package com.acuo.persist.services;

import com.acuo.persist.entity.CallStatus;
import com.acuo.persist.entity.MarginStatement;
import com.acuo.persist.ids.ClientId;
import com.acuo.persist.ids.MarginStatementId;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

@Transactional
public class MarginStatementServiceImpl extends GenericService<MarginStatement> implements MarginStatementService {

    @Override
    public Class<MarginStatement> getEntityType() {
        return MarginStatement.class;
    }

    @Override
    public Iterable<MarginStatement> allStatementsFor(ClientId clientId, CallStatus... statuses) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m)<-[*1..2]-(mc:StatementItem)-[:LAST]->(step:Step) " +
                "WHERE step.status IN {statuses} RETURN m, nodes(p), rels(p)";
        return session.query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString(), "statuses", statuses));
    }

    @Override
    public Iterable<MarginStatement> allStatementsForClient(ClientId clientId) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                "(m) <-[*1..2]-(mc:StatementItem)-[:LAST]->(step:Step) RETURN m, nodes(p), rels(p)";
        return session.query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString()));
    }

    @Override
    public MarginStatement statementFor(MarginStatementId marginStatementId, CallStatus... statuses) {
        String query =
                "MATCH p=(f:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                "(m:MarginStatement {id:{marginStatementId}})<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "WHERE step.status IN {statuses} " +
                "RETURN m, nodes(p), rels(p)";
        return session.queryForObject(MarginStatement.class, query, ImmutableMap.of("marginStatementId", marginStatementId.toString(), "statuses", statuses));
    }

    @Override
    public Iterable<MarginStatement> allStatementsForRecon(ClientId clientId) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement)<-[]-(mc:StatementItem)-[:LAST]->(step:Step) where step.status in ['Unrecon']" +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m)<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "RETURN m, mc, nodes(p), rels(p)";
        return session.query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString()));
    }

    @Override
    public Iterable<MarginStatement> allUnmatchedStatements(ClientId clientId) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                "(m) <-[*1..2]-(mc:MarginCall)-[:LAST]->(step:Step {status:'Unrecon'}) " +
                "WHERE NOT exists((mc)-[:MATCHED_TO_EXPECTED]->()) " +
                "RETURN m, nodes(p), rels(p)";
        return session.query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString()));
    }

    @Override
    public MarginStatement statementForRecon(MarginStatementId marginStatementId) {
        String query =
                "MATCH (m:MarginStatement {id:{marginStatementId}})<-[]-(mc:MarginCall)-[:LAST]->(step:Step {status:'Unrecon'}) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m)<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "RETURN m, mc, nodes(p), rels(p)";
        return session.queryForObject(MarginStatement.class, query, ImmutableMap.of("marginStatementId", marginStatementId.toString()));
    }

    @Override
    public MarginStatement statementOf(String callId) {
        String query =
                "MATCH (m:MarginStatement)<-[*1..2]-(mc:MarginCall {id:{callId}}) " +
                "WITH m " +
                "MATCH p=(f:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m)<-[*1..2]-(mc:MarginCall) " +
                "RETURN m, nodes(p), rels(p)";
        return session.queryForObject(MarginStatement.class, query, ImmutableMap.of("callId", callId));
    }
}