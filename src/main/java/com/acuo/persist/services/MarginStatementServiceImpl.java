package com.acuo.persist.services;

import com.acuo.common.model.ids.ClientId;
import com.acuo.common.model.ids.MarginStatementId;
import com.acuo.persist.entity.Agreement;
import com.acuo.persist.entity.MarginStatement;
import com.acuo.persist.entity.StatementItem;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.Session;

import javax.inject.Provider;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.acuo.persist.entity.enums.StatementStatus.*;
import static java.util.Arrays.asList;

@Slf4j
public class MarginStatementServiceImpl extends AbstractService<MarginStatement, String> implements MarginStatementService {

    private final StatementItemService statementItemService;

    @Inject
    public MarginStatementServiceImpl(Provider<Session> session,
                                      StatementItemService statementItemService) {
        super(session);
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
        return dao.getSession().query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString(), "statuses", statuses));
    }

    @Override
    @Transactional
    public Iterable<MarginStatement> allStatementsForClient(ClientId clientId) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                "(m) <-[*1..2]-(mc:StatementItem)-[:LAST]->(step:Step) RETURN m, nodes(p), relationships(p)";
        return dao.getSession().query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString()));
    }

    @Override
    @Transactional
    public MarginStatement statementFor(MarginStatementId marginStatementId, StatementStatus... statuses) {
        String query =
                "MATCH p=(f:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                "(m:MarginStatement {id:{marginStatementId}})<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "WHERE step.status IN {statuses} " +
                "RETURN m, nodes(p), relationships(p)";
        return dao.getSession().queryForObject(MarginStatement.class, query, ImmutableMap.of("marginStatementId", marginStatementId.toString(), "statuses", statuses));
    }

    @Override
    @Transactional
    public Iterable<MarginStatement> allStatementsForRecon(ClientId clientId) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement)<-[]-(mc:StatementItem)-[:LAST]->(step:Step) " +
                "WHERE step.status in ['Unrecon','Expected','MatchedToReceived']" +
                "WITH m, mc " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m)<-[]-(mc)-[:LAST]->(step:Step) " +
                "RETURN m, mc, nodes(p), relationships(p)";
        return dao.getSession().query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString()));
    }

    @Override
    @Transactional
    public Iterable<MarginStatement> allUnmatchedStatements(ClientId clientId) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-" +
                "(m) <-[*1..2]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "WHERE NOT exists((mc)-[:MATCHED_TO]->()) " +
                "AND step.status in ['Unrecon','Expected'] " +
                "RETURN p, nodes(p), relationships(p)";
        return dao.getSession().query(MarginStatement.class, query, ImmutableMap.of("clientId", clientId.toString()));
    }

    @Override
    @Transactional
    public MarginStatement statementForRecon(MarginStatementId marginStatementId) {
        String query =
                "MATCH (m:MarginStatement {id:{marginStatementId}})<-[]-(mc:MarginCall)-[:LAST]->(step:Step {status:'Unrecon'}) " +
                "WITH m " +
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m)<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "RETURN m, mc, nodes(p), relationships(p)";
        return dao.getSession().queryForObject(MarginStatement.class, query, ImmutableMap.of("marginStatementId", marginStatementId.toString()));
    }

    @Override
    @Transactional
    public MarginStatement statementOf(String callId) {
        String query =
                "MATCH (m:MarginStatement)<-[*1..2]-(mc:MarginCall {id:{callId}}) " +
                "WITH m " +
                "MATCH p=(f:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m)<-[*1..2]-(mc:MarginCall) " +
                "RETURN m, nodes(p), relationships(p)";
        return dao.getSession().queryForObject(MarginStatement.class, query, ImmutableMap.of("callId", callId));
    }

    @Override
    @Transactional
    public Iterable<MarginStatement> statementOf(String... callIds) {
        String query =
                "MATCH (m:MarginStatement)<-[*1..2]-(mc:MarginCall) " +
                "WHERE mc.id IN {ids} " +
                "WITH m " +
                "MATCH p=(f:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[:STEMS_FROM]-(m)<-[*1..2]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "RETURN m, nodes(p), relationships(p)";
        return dao.getSession().query(MarginStatement.class, query, ImmutableMap.of("ids", callIds));
    }

    @Override
    @Transactional
    public void reconcile(MarginStatementId marginStatementId, Double amount) {
        log.info("reconciling all items for margin statement [{}]", marginStatementId);
        MarginStatement marginStatement = find(marginStatementId.toString(), 2);
        Set<StatementItem> calls = filter(marginStatement.getStatementItems(), Unrecon, MatchedToReceived);
        for (StatementItem call : calls) {
            log.debug("parent call {} and children {}", call);
            if (Unrecon.equals(call.getLastStep().getStatus())) {
                statementItemService.setStatus(call.getItemId(), Reconciled);
            }
            if(MatchedToReceived.equals(call.getLastStep().getStatus())) {
                statementItemService.setStatus(call.getItemId(), Closed);
            }
        }
        log.info("margin statement {} reconciled",marginStatement);
    }

    @Override
    @Transactional
    public void match(MarginStatementId fromId, MarginStatementId toId) {
        //MarginStatement from = find(fromId.toString());
    }

    @Override
    @Transactional
    public MarginStatement getMarginStatement(Agreement agreement, LocalDate callDate) {
        String query =
                "MATCH p=(firm:Firm)-[:MANAGES]-(l1:LegalEntity)-[:CLIENT_SIGNS|COUNTERPARTY_SIGNS]-" +
                "(a:Agreement {id:{agreementId}})<-[:STEMS_FROM]-(m:MarginStatement {date:{date}})" +
                "-[:SENT_FROM|DIRECTED_TO]->(l2:LegalEntity)" +
                "RETURN m, nodes(p), relationships(p)";
        String dateStr = new LocalDateConverter().toGraphProperty(callDate);
        String agreementId = agreement.getAgreementId();
        ImmutableMap<String, String> parameters = ImmutableMap.of("agreementId", agreementId,
                                                                  "date", dateStr);
        return dao.getSession().queryForObject(MarginStatement.class, query, parameters);
    }

    @Override
    @Transactional
    public MarginStatement getOrCreateMarginStatement(Agreement agreement, LocalDate callDate) {
        MarginStatement marginStatement = getMarginStatement(agreement, callDate);
        if (marginStatement == null) {
            marginStatement = new MarginStatement(agreement, callDate);
            marginStatement = save(marginStatement);
        }
        return marginStatement;
    }

    private Set<StatementItem> filter(Set<StatementItem> calls, StatementStatus... statuses) {
        return calls.stream()
                .filter(mc -> asList(statuses).contains(mc.getLastStep().getStatus()))
                .collect(Collectors.toSet());
    }

    /**
     * @deprecated  {@link StatementItemService#setStatus(String, StatementStatus)}
     */
    @Deprecated
    @Override
    @Transactional
    public <T extends StatementItem> T setStatus(String statementItemId, StatementStatus status) {
        return statementItemService.setStatus(statementItemId, status);
    }

    @Override
    @Transactional
    public Long count(StatementStatus status) {
        String query =
                "MATCH p=(agreement:Agreement)<-[:STEMS_FROM]-(ms:MarginStatement)<-[:PART_OF]-(s:StatementItem {direction:'OUT'})" +
                "-[:LAST]->(step:Step {status:{status}}) " +
                "RETURN ms, nodes(p), relationships(p)";
        ImmutableMap<String, Object> parameters = ImmutableMap.of("status", status.name());
        Iterable<MarginStatement> marginStatements = dao.getSession().query(MarginStatement.class, query, parameters);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime max = now.plusHours(36);
        LocalDateTime min = now.minusHours(36);
        return StreamSupport.stream(marginStatements.spliterator(), true)
                .filter(marginStatement -> {
                    LocalDateTime localDateTime = LocalDateTime.of(marginStatement.getDate(), marginStatement.getAgreement().getNotificationTime());
                    return localDateTime.isAfter(min) && localDateTime.isBefore(max);
                }).count();
    }
}