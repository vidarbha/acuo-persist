package com.acuo.persist.services;

import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.StatementItem;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.ids.MarginStatementId;
import com.acuo.persist.spring.Call;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.StreamSupport;


public class MarginCallServiceImpl extends GenericService<MarginCall, String> implements MarginCallService {

    @Inject
    StatementItemService statementItemService;

    @Override
    public Class<MarginCall> getEntityType() {
        return MarginCall.class;
    }

    @Override
    @Transactional
    public Iterable<MarginCall> allCallsFor(String clientId, StatementStatus... statuses) {
        String query =
                "MATCH p=(:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[r:CLIENT_SIGNS]->(a:Agreement)<-[:STEMS_FROM]-" +
                        "(m:MarginStatement)<-[*1..2]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                        "WHERE step.status IN {statuses} " +
                        "RETURN mc, nodes(p), relationships(p)";
        return sessionProvider.get().query(MarginCall.class, query, ImmutableMap.of("clientId", clientId, "statuses", statuses));
    }

    @Override
    @Transactional
    public Iterable<MarginCall> callFor(String marginStatementId, StatementStatus... statuses) {
        String query =
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m:MarginStatement {id:{msId}})<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "WHERE step.status IN {statuses} " +
                "RETURN mc, nodes(p), relationships(p)";
        return sessionProvider.get().query(MarginCall.class, query, ImmutableMap.of("msId", marginStatementId, "statuses", statuses));
    }

    @Override
    @Deprecated
    @Transactional
    public Iterable<MarginCall> callsForExpected(String marginStatementId) {
        String query = "MATCH (:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m:MarginStatement {id:{msId}})<-[]-(mc1:MarginCall)-[:LAST]->(step:Step {status:'Unrecon'}) " +
                "MATCH (mc2:MarginCall)<-[:MATCHED_TO]-(mc1) " +
                "return mc2";
        return sessionProvider.get().query(MarginCall.class, query, ImmutableMap.of("msId", marginStatementId));
    }

    @Override
    @Transactional
    public Iterable<MarginCall> allExpectedCallsFor(MarginStatementId marginStatementId) {
        String query =  "MATCH (m:MarginStatement {id:{msId}})<-[]-(mc:MarginCall)-[:LAST]->(step:Step {status:'Expected'}) " +
                        "return mc";
        return sessionProvider.get().query(MarginCall.class, query, ImmutableMap.of("msId", marginStatementId.toString()));
    }

    @Override
    @Transactional
    public Iterable<Call> notToUseYetallCallsFor(String clientId, String dateTime) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[r:CLIENT_SIGNS]->(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement {date:{dateTime}}) " +
                        "WITH a, m " +
                        "MATCH (m)<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                        "WITH {category:a.type, type:mc.callType, status:step.status, balance: mc.balanceAmount, excess: mc.excessAmount} AS Call " +
                        "RETURN Call";
        return sessionProvider.get().query(Call.class, query, ImmutableMap.of("clientId", clientId, "dateTime", dateTime));
    }

    private static final BiPredicate<MarginCall, MarginCall> MATCHING_CALLS =
            (clientCall, cptyCall) ->
                clientCall.getCallDate().equals(cptyCall.getCallDate()) &&
                clientCall.getMarginType().equals(cptyCall.getMarginType());

    @Override
    @Transactional
    public void matchToExpected(String callId) {
        MarginCall cptyCall = find(callId, 1);
        StatementItem matchedItem = cptyCall.getMatchedItem();
        if (matchedItem != null) {
            return;
        }
        final MarginStatementId marginStatementId = MarginStatementId.fromString(cptyCall.getMarginStatement().getStatementId());
        final Iterable<MarginCall> marginCalls = allExpectedCallsFor(marginStatementId);

        final Optional<MarginCall> expected = StreamSupport.stream(marginCalls.spliterator(), false)
                .filter(clientCall -> MATCHING_CALLS.test(clientCall, cptyCall))
                .findFirst();
        if (expected.isPresent()) {
            cptyCall.setMatchedItem(expected.get());
            statementItemService.setStatus(expected.get().getItemId(), StatementStatus.MatchedToReceived);
            save(cptyCall);
        }
    }
}