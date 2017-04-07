package com.acuo.persist.services;

import com.acuo.persist.entity.Agreement;
import com.acuo.persist.entity.MarginStatement;
import com.acuo.persist.entity.enums.StatementDirection;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.ids.ClientId;
import com.acuo.persist.ids.MarginStatementId;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

import java.time.LocalDate;

public class MarginStatementServiceImpl extends GenericService<MarginStatement> implements MarginStatementService {

    @Override
    public Class<MarginStatement> getEntityType() {
        return MarginStatement.class;
    }

    @Override
    @Transactional
    public Iterable<MarginStatement> allStatementsFor(ClientId clientId, StatementStatus... statuses) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m)<-[*1..2]-(mc:StatementItem)-[:LAST]->(step:Step) " +
                "WHERE step.status IN {statuses} RETURN m, nodes(p), rels(p)";
        return sessionProvider.get().query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString(), "statuses", statuses));
    }

    @Override
    @Transactional
    public Iterable<MarginStatement> allStatementsForClient(ClientId clientId) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                "(m) <-[*1..2]-(mc:StatementItem)-[:LAST]->(step:Step) RETURN m, nodes(p), rels(p)";
        return sessionProvider.get().query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString()));
    }

    @Override
    @Transactional
    public MarginStatement statementFor(MarginStatementId marginStatementId, StatementStatus... statuses) {
        String query =
                "MATCH p=(f:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                "(m:MarginStatement {id:{marginStatementId}})<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "WHERE step.status IN {statuses} " +
                "RETURN m, nodes(p), rels(p)";
        return sessionProvider.get().queryForObject(MarginStatement.class, query, ImmutableMap.of("marginStatementId", marginStatementId.toString(), "statuses", statuses));
    }

    @Override
    @Transactional
    public Iterable<MarginStatement> allStatementsForRecon(ClientId clientId) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement)<-[]-(mc:StatementItem)-[:LAST]->(step:Step) where step.status in ['Unrecon']" +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m)<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "RETURN m, mc, nodes(p), rels(p)";
        return sessionProvider.get().query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString()));
    }

    @Override
    @Transactional
    public Iterable<MarginStatement> allUnmatchedStatements(ClientId clientId) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                "(m) <-[*1..2]-(mc:MarginCall)-[:LAST]->(step:Step {status:'Unrecon'}) " +
                "WHERE NOT exists((mc)-[:MATCHED_TO_EXPECTED]->()) " +
                "RETURN m, nodes(p), rels(p)";
        return sessionProvider.get().query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString()));
    }

    @Override
    @Transactional
    public MarginStatement statementForRecon(MarginStatementId marginStatementId) {
        String query =
                "MATCH (m:MarginStatement {id:{marginStatementId}})<-[]-(mc:MarginCall)-[:LAST]->(step:Step {status:'Unrecon'}) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m)<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "RETURN m, mc, nodes(p), rels(p)";
        return sessionProvider.get().queryForObject(MarginStatement.class, query, ImmutableMap.of("marginStatementId", marginStatementId.toString()));
    }

    @Override
    @Transactional
    public MarginStatement statementOf(String callId) {
        String query =
                "MATCH (m:MarginStatement)<-[*1..2]-(mc:MarginCall {id:{callId}}) " +
                "WITH m " +
                "MATCH p=(f:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m)<-[*1..2]-(mc:MarginCall) " +
                "RETURN m, nodes(p), rels(p)";
        return sessionProvider.get().queryForObject(MarginStatement.class, query, ImmutableMap.of("callId", callId));
    }

    @Override
    @Transactional
    public void match(MarginStatementId fromId, MarginStatementId toId) {
        MarginStatement from = findById(fromId.toString());
    }

    @Override
    @Transactional
    public MarginStatement getMarginStatement(Agreement agreement, LocalDate date, StatementDirection direction) {
        String query = "MATCH p=(a:Agreement {id:{agreementId}})<-[:STEMS_FROM]-(m:MarginStatement {date:{date}, direction:{direction}}) " +
                "RETURN m, nodes(p), rels(p)";
        String dateStr = new LocalDateConverter().toGraphProperty(date);
        String agreementId = agreement.getAgreementId();
        String dir = direction.name();
        ImmutableMap<String, String> parameters = ImmutableMap.of("agreementId", agreementId,
                                                                  "date", dateStr,
                                                                  "direction", dir);
        return sessionProvider.get().queryForObject(MarginStatement.class, query, parameters);
    }

    @Override
    @Transactional
    public MarginStatement getOrCreateMarginStatement(Agreement agreement, LocalDate date, StatementDirection direction) {
        MarginStatement marginStatement = getMarginStatement(agreement, date, direction);
        if (marginStatement == null) {
            marginStatement = new MarginStatement(agreement, date, direction);
            marginStatement = save(marginStatement);
        }
        return marginStatement;
    }
}