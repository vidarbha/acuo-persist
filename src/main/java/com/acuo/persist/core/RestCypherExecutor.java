package com.acuo.persist.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

public class RestCypherExecutor implements CypherExecutor {

    private final String restEndpoint;
    private final Logger log;
    private final String encodedPassword;

    public RestCypherExecutor(String restEndpoint) {
        this(restEndpoint, "");
    }

    public RestCypherExecutor(String restEndpoint, String encodedPassword) {
        this.restEndpoint = restEndpoint;
        this.encodedPassword = encodedPassword;
        this.log = LoggerFactory.getLogger(getClass());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.rbccm.fi.mdds.dao.neo4j.CypherExecutor#executeStatement(java.lang.
     * String, java.util.Map)
     */
    @Override
    public Collection<ICypherResult> executeStatement(String query) {
        Collection<Map<String, Object>> results = executeRestQuery(query);
        List<ICypherResult> cypherResults = new ArrayList<ICypherResult>(results.size());
        for (Map<String, Object> result : results) {
            cypherResults.add(new CypherResult(result));
        }
        return cypherResults;
    }

    @Override
    public Collection<ICypherResult> executeStatements(Collection<String> queries) {
        Collection<Map<String, Object>> results = executeRestQuery(StringUtils.join(queries, "\n"));
        List<ICypherResult> cypherResults = new ArrayList<ICypherResult>(results.size());
        for (Map<String, Object> result : results) {
            cypherResults.add(new CypherResult(result));
        }
        return cypherResults;
    }

    @Override
    public Collection<ICypherResult> executeParameterisedStatement(String query, Map<String, Object> parameters) {
        String expandedQuery = substituteQueryParameterValues(query, parameters);
        return executeStatement(StringEscapeUtils.escapeJava(expandedQuery));
    }

    private Collection<Map<String, Object>> executeRestQuery(String query) {

        final String txUri = "http://" + restEndpoint + "/db/data/transaction/commit";
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(txUri);

        Entity<String> payload = Entity.entity("{\"statements\" : [ {\"statement\" : \"" + query + "\"} ]}",
                MediaType.APPLICATION_JSON);

        Invocation.Builder builder = resource.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        builder.header(HttpHeaders.AUTHORIZATION, encodedPassword);
        Response response = builder.post(payload);
        String jsonReturn = response.readEntity(String.class);
        log.info(String.format(
                "POST [%s] to [%s], status code [%d], returned data: " + System.getProperty("line.separator") + "%s",
                payload, txUri, response.getStatus(), jsonReturn));

        response.close();
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(jsonReturn);
            JsonNode resultsNode = root.get("results");
            JsonNode errorNode = root.get("errors");
            if (errorNode.size() != 0) {
                String code = errorNode.get(0).get("code").asText();
                String message = errorNode.get(0).get("message").asText();
                throw new PersistenceException(
                        String.format("Error executing statement. Code %s Message %s", code, message));
            }
            if (resultsNode.size() == 0) {
                return results;
            }
            JsonNode columns = resultsNode.get(0).get("columns");
            JsonNode values = resultsNode.get(0).get("data");

            for (int v = 0; v < values.size(); v++) {
                JsonNode row = values.get(v).get("row");
                Map<String, Object> rowResults = new HashMap<String, Object>();
                for (int i = 0; i < columns.size(); i++) {
                    JsonNode value = row.get(i);
                    if (value.isTextual()) {
                        rowResults.put(columns.get(i).asText(), row.get(i).asText());
                    } else {
                        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
                        };
                        Map<String, Object> temp = mapper.readValue(value.asText(), typeReference);
                        rowResults.putAll(temp);
                    }
                }
                results.add(rowResults);

            }
        } catch (IOException ioe) {
            throw new PersistenceException(ioe);
        }
        return results;
    }
}
