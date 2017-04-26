package com.acuo.persist.services;

import com.acuo.persist.entity.Agreement;
import com.acuo.persist.entity.MarginStatement;
import com.acuo.persist.entity.StatementItem;
import com.acuo.persist.entity.enums.StatementDirection;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.ids.ClientId;
import com.acuo.persist.ids.MarginStatementId;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class MarginStatementServiceImpl extends GenericService<MarginStatement, String> implements MarginStatementService {

    private final StatementItemService statementItemService;

    @Inject
    public MarginStatementServiceImpl(StatementItemService statementItemService) {
        this.statementItemService = statementItemService;
    }

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
                "WHERE step.status IN {statuses} RETURN m, nodes(p), relationships(p)";
        return sessionProvider.get().query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString(), "statuses", statuses));
    }

    @Override
    @Transactional
    public Iterable<MarginStatement> allStatementsForClient(ClientId clientId) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                "(m) <-[*1..2]-(mc:StatementItem)-[:LAST]->(step:Step) RETURN m, nodes(p), relationships(p)";
        return sessionProvider.get().query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString()));
    }

    @Override
    @Transactional
    public MarginStatement statementFor(MarginStatementId marginStatementId, StatementStatus... statuses) {
        String query =
                "MATCH p=(f:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                "(m:MarginStatement {id:{marginStatementId}})<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "WHERE step.status IN {statuses} " +
                "RETURN m, nodes(p), relationships(p)";
        return sessionProvider.get().queryForObject(MarginStatement.class, query, ImmutableMap.of("marginStatementId", marginStatementId.toString(), "statuses", statuses));
    }

    @Override
    @Transactional
    public Iterable<MarginStatement> allStatementsForRecon(ClientId clientId) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement)<-[]-(mc:StatementItem)-[:LAST]->(step:Step) where step.status in ['Unrecon']" +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m)<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "RETURN m, mc, nodes(p), relationships(p)";
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
                "WHERE NOT exists((mc)-[:MATCHED_TO]->()) " +
                "RETURN m, nodes(p), relationships(p)";
        return sessionProvider.get().query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString()));
    }

    @Override
    @Transactional
    public MarginStatement statementForRecon(MarginStatementId marginStatementId) {
        String query =
                "MATCH (m:MarginStatement {id:{marginStatementId}})<-[]-(mc:MarginCall)-[:LAST]->(step:Step {status:'Unrecon'}) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m)<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "RETURN m, mc, nodes(p), relationships(p)";
        return sessionProvider.get().queryForObject(MarginStatement.class, query, ImmutableMap.of("marginStatementId", marginStatementId.toString()));
    }

    @Override
    @Transactional
    public MarginStatement statementOf(String callId) {
        String query =
                "MATCH (m:MarginStatement)<-[*1..2]-(mc:MarginCall {id:{callId}}) " +
                "WITH m " +
                "MATCH p=(f:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m)<-[*1..2]-(mc:MarginCall) " +
                "RETURN m, nodes(p), relationships(p)";
        return sessionProvider.get().queryForObject(MarginStatement.class, query, ImmutableMap.of("callId", callId));
    }

    @Override
    @Transactional
    public void reconcile(MarginStatementId marginStatementId, Double amount) {
        log.info("reconciling all items for margin statement [{}]", marginStatementId);
        MarginStatement marginStatement = find(marginStatementId.toString(), 2);
        Set<StatementItem> receviedMarginCalls = filter(marginStatement.getStatementItems(), StatementStatus.Unrecon);
        for (StatementItem marginCall : receviedMarginCalls) {
            log.debug("parent call {} and children {}", marginCall);
            statementItemService.setStatus(marginCall.getItemId(), StatementStatus.Reconciled);
        }
        log.info("margin statement {} reconciled",marginStatement);
    }

    @Override
    @Transactional
    public void match(MarginStatementId fromId, MarginStatementId toId) {
        MarginStatement from = find(fromId.toString());
    }

    @Override
    @Transactional
    public MarginStatement getMarginStatement(Agreement agreement, LocalDate callDate, StatementDirection direction) {
        String query = "MATCH p=(a:Agreement {id:{agreementId}})<-[:STEMS_FROM]-(m:MarginStatement {date:{date}, direction:{direction}}) " +
                "RETURN m, nodes(p), relationships(p)";
        String dateStr = new LocalDateConverter().toGraphProperty(callDate);
        String agreementId = agreement.getAgreementId();
        String dir = direction.name();
        ImmutableMap<String, String> parameters = ImmutableMap.of("agreementId", agreementId,
                                                                  "date", dateStr,
                                                                  "direction", dir);
        return sessionProvider.get().queryForObject(MarginStatement.class, query, parameters);
    }

    @Override
    @Transactional
    public MarginStatement getOrCreateMarginStatement(Agreement agreement, LocalDate callDate, StatementDirection direction) {
        MarginStatement marginStatement = getMarginStatement(agreement, callDate, direction);
        if (marginStatement == null) {
            marginStatement = new MarginStatement(agreement, callDate, direction);
            marginStatement = save(marginStatement);
        }
        return marginStatement;
    }

    private Set<StatementItem> filter(Set<StatementItem> calls, StatementStatus status) {
        return calls.stream()
                .filter(mc -> status.equals(mc.getLastStep().getStatus()))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void setStatus(String statementItemId, StatementStatus status) {
        statementItemService.setStatus(statementItemId, status);
    }
}