package com.acuo.persist.services;

import com.acuo.persist.entity.CallStatus;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.spring.Call;
import com.acuo.persist.entity.MarginStatement;
import com.google.common.collect.ImmutableMap;

public class MarginStatementServiceImpl extends GenericService<MarginStatement> implements MarginStatementService {

    @Override
    public Class<MarginStatement> getEntityType() {
        return MarginStatement.class;
    }

    @Override
    public Iterable<MarginStatement> allStatementsFor(String clientId, CallStatus... statuses) {
        String query = "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m)<-[*1..2]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "WHERE step.status IN {statuses} RETURN m, nodes(p), rels(p)";
        return session.query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId, "statuses", statuses));
    }

    @Override
    public Iterable<MarginStatement> allStatementsForClient(String clientId) {
        String query = "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement) " +
                        "WITH m " +
                        "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                        "(m) <-[*1..2]-(mc:MarginCall)-[:LAST]->(step:Step) RETURN m, nodes(p), rels(p)";
        return session.query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId));
    }

    @Override
    public MarginStatement statementFor(String marginStatementId, CallStatus... statuses) {
        String query =
                "MATCH p=(f:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                        "(m:MarginStatement {id:{marginStatementId}})<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                        "WHERE step.status IN {statuses} " +
                        "RETURN m, nodes(p), rels(p)";
        return session.queryForObject(MarginStatement.class, query, ImmutableMap.of("marginStatementId", marginStatementId, "statuses", statuses));
    }

    @Override
    public Iterable<Call> allCallsFor(String clientId, String dateTime) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[r:CLIENT_SIGNS]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement {date:{dateTime}}) " +
                        "WITH a, m " +
                        "MATCH (m)<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                        "WITH {category:a.type, type:mc.callType, status:step.status, balance: mc.balanceAmount, excess: mc.excessAmount} AS Call " +
                        "RETURN Call";
        return session.query(Call.class, query, ImmutableMap.of("clientId", clientId, "dateTime", dateTime));
    }

    @Override
    public Iterable<MarginStatement> allStatementsForRecon(String clientId)
    {
        String query = "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m)<-[]-(mc:MarginCall)-[:LAST]->(step:Step {status:'Unrecon'}) " +
                "RETURN m, nodes(p), rels(p)";
        return session.query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId));
    }

    @Override
    public Iterable<MarginCall> statementForExpected(String marginStatementId)
    {
        String query = "MATCH (:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m:MarginStatement {id:{msId}})<-[]-(mc1:MarginCall)-[:LAST]->(step:Step {status:'Unrecon'})" +
                "MATCH (mc2:MarginCall)<-[MATCHED_TO_EXPECTED]-(mc1) " +
                "return mc2";
        return session.query(MarginCall.class, query, ImmutableMap.of("msId", marginStatementId));
    }

    public Iterable<MarginCall> allCallForReconciled(String marginStatementId)
    {
        String query = "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m:MarginStatement {id:{msId}})<-[*1..2]-(mc:MarginCall)-[:LAST]->(step:Step) WHERE step.status IN ['Reconciled'] RETURN mc";
        return session.query(MarginCall.class, query, ImmutableMap.of("msId", marginStatementId));
    }
}