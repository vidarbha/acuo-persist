package com.acuo.persist.services;

import com.acuo.common.model.margin.Dispute;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.StatementItem;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.ids.MarginStatementId;
import com.acuo.persist.spring.Call;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.model.Result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

    @Override
    @Transactional
    public List<com.acuo.common.model.margin.MarginCall> getDisputeMarginCall(String marginStatementId)
    {
        List<com.acuo.common.model.margin.MarginCall> marginCalls = new ArrayList<>();
        String query = "MATCH (ms:MarginStatement {id:{id}})<-[:ON]-(d:Dispute) " +
                "WHERE d.AgreedAmount = 0  " +
                "MATCH (mc:MarginCall)-[:PART_OF]->(ms) " +
                "RETURN mc.ampId, d.AgreedAmount, d.disputeReasonCode, d.comments, d.MtM, d.exposure";

        Result result = sessionProvider.get().query(query, ImmutableMap.of("id", marginStatementId));
        result.forEach(map -> marginCalls.add(buildDisputeMarginCall(map)));
        return marginCalls;
    }

    private com.acuo.common.model.margin.MarginCall buildDisputeMarginCall(Map<String, Object> map)
    {
        com.acuo.common.model.margin.MarginCall marginCall = new com.acuo.common.model.margin.MarginCall();
        marginCall.setAmpId((String)map.get("mc.ampId"));
        marginCall.setAgreedAmount((Double)map.get("d.AgreedAmount"));
        Dispute dispute = new Dispute();
        marginCall.setDispute(dispute);
        Set<Types.DisputeReasonCode> disputeReasonCodeSet = new HashSet<>();
        dispute.setDisputeReasonCodes(disputeReasonCodeSet);
        disputeReasonCodeSet.add(Types.DisputeReasonCode.valueOf((String)map.get("d.disputeReasonCode")));
        dispute.setComments((String)map.get("d.comments"));
        dispute.setMtm((Double)map.get("d.MtM"));
        marginCall.setExposure((Double)map.get("d.exposure"));
        return marginCall;
    }

    @Override
    @Transactional
    public com.acuo.common.model.margin.MarginCall getPledgeMarginCall(String marginCallId)
    {
        com.acuo.common.model.margin.MarginCall marginCall = new com.acuo.common.model.margin.MarginCall();
        String query = String.format(
                "MATCH (mc:MarginCall {id:{id}})<-[:GENERATED_BY]-(at:AssetTransfer)-[:OF]->(a:Asset) " +
                "MATCH (ca:CustodianAccount)<-[:FROM]-(at) " +
                "RETURN mc.ampId, a.id, a.idType, a.currency, at.quantity, at.value");

        Result result = sessionProvider.get().query(query, ImmutableMap.of("id", marginCallId));
        Map<String, Object> map = result.iterator().next();
        if(map != null)
        {
            marginCall.setAmpId((String)map.get("mc.ampId"));
            marginCall.setCurrency(com.opengamma.strata.basics.currency.Currency.of((String)map.get("a.currency")));
        }
        return marginCall;
    }
}