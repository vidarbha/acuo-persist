package com.acuo.persist.core;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.*;
import java.util.Map.Entry;

public class EmbeddedCypherExecutor implements CypherExecutor {

    private final GraphDatabaseService graphDatabaseService;

    public EmbeddedCypherExecutor(GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
    }

    @Override
    public Collection<ICypherResult> executeParameterisedStatement(String query, Map<String, Object> parameters) {
        try (Transaction tx = graphDatabaseService.beginTx()) {
            String stringifiedQuery = substituteQueryParameterValues(query, parameters);
            Result result = graphDatabaseService.execute(stringifiedQuery);
            Collection<ICypherResult> results = getResults(result);
            tx.success();
            return results;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.rbccm.fi.mdds.dao.neo4j.CypherExecutor#executeStatement(java.lang.
     * String)
     */
    @Override
    public Collection<ICypherResult> executeStatement(String query) {
        try (Transaction tx = graphDatabaseService.beginTx()) {
            Result result = graphDatabaseService.execute(query);
            Collection<ICypherResult> results = getResults(result);
            tx.success();
            return results;
        }
    }

    @Override
    public Collection<ICypherResult> executeStatements(Collection<String> queries) {
        try (Transaction graphTx = graphDatabaseService.beginTx()) {
            List<ICypherResult> results = new ArrayList<ICypherResult>();
            for (String s : queries) {
                Result result = graphDatabaseService.execute(s);
                results.addAll(getResults(result));
            }
            graphTx.success();
            return results;
        }
    }

    private Collection<ICypherResult> getResults(Result result) {
        List<ICypherResult> results = new ArrayList<ICypherResult>();
        List<Map<String, Object>> list = new ArrayList<>();
        result.forEachRemaining(list::add);
        for (Map<String, Object> row : list) {
            Map<String, Object> cypherResult = new HashMap<String, Object>();
            // Yuck
            for (Entry<String, Object> rowEntry : row.entrySet()) {
                if (rowEntry.getValue() instanceof Node) {
                    Node n = (Node) rowEntry.getValue();
                    for (String key : n.getPropertyKeys()) {
                        cypherResult.put(key, n.getProperty(key));
                    }
                } else {
                    cypherResult.put(rowEntry.getKey(), rowEntry.getValue());
                }
            }
            results.add(new CypherResult(cypherResult));

        }
        return results;
    }

}
