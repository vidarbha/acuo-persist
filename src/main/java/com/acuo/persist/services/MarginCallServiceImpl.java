package com.acuo.persist.services;

import com.acuo.common.ids.MarginStatementId;
import com.acuo.common.model.margin.Dispute;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.StatementItem;
import com.acuo.persist.entity.enums.Side;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.spring.Call;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.opengamma.strata.basics.currency.Currency;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;

import javax.inject.Provider;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.StreamSupport;


public class MarginCallServiceImpl extends AbstractService<MarginCall, String> implements MarginCallService {

    @Inject
    private final StatementItemService statementItemService;

    @javax.inject.Inject
    public MarginCallServiceImpl(Provider<Session> session,
                                 StatementItemService statementItemService) {
        super(session);
        this.statementItemService = statementItemService;
    }

    @Override
    public Class<MarginCall> getEntityType() {
        return MarginCall.class;
    }

    @Override
    @Transactional
    public Iterable<MarginCall> allCallsFor(String clientId, StatementStatus... statuses) {
        String query =
                "MATCH p=(:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[r:CLIENT_SIGNS]->" +
                "(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement)<-[*1..2]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "WHERE step.status IN {statuses} " +
                "RETURN mc, nodes(p), relationships(p)";
        ImmutableMap<String, Serializable> parameters = ImmutableMap.of("clientId", clientId, "statuses", statuses);
        return dao.getSession().query(MarginCall.class, query, parameters);
    }

    @Override
    @Transactional
    public Iterable<MarginCall> callFor(String marginStatementId, StatementStatus... statuses) {
        String query =
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m:MarginStatement {id:{msId}})" +
                "<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "WHERE step.status IN {statuses} " +
                "RETURN mc, nodes(p), relationships(p)";
        ImmutableMap<String, Serializable> parameters = ImmutableMap.of("msId", marginStatementId, "statuses", statuses);
        return dao.getSession().query(MarginCall.class, query, parameters);
    }

    @Override
    @Transactional
    public MarginCall callByAmpId(String ampId) {
        String query =
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m:MarginStatement)" +
                "<-[]-(mc:MarginCall {ampId:{id}})-[:LAST]->(step:Step) " +
                "WITH mc, m, p " +
                "MATCH a=(m)-[:SENT_FROM|DIRECTED_TO]->()-[:ACCESSES]->() " +
                "RETURN mc, nodes(p), relationships(p), nodes(a), relationships(a)";
        return dao.getSession().queryForObject(MarginCall.class, query, ImmutableMap.of("id", ampId));
    }

    @Override
    @Transactional
    public MarginCall callByAmpIdAndSide(String ampId, Side side) {
        String query =
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m:MarginStatement)" +
                "<-[]-(mc:MarginCall {ampId:{id}, side:{side}})-[:LAST]->(step:Step) " +
                "WITH mc, m, p " +
                "MATCH a=(m)-[:SENT_FROM|DIRECTED_TO]->()-[:ACCESSES]->() " +
                "RETURN mc, nodes(p), relationships(p), nodes(a), relationships(a)";
        ImmutableMap<String, String> parameters = ImmutableMap.of("id", ampId, "side", side.name());
        return dao.getSession().queryForObject(MarginCall.class, query, parameters);
    }

    @Override
    @Transactional
    public MarginCall callById(String callId) {
        String query =
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m:MarginStatement)" +
                "<-[]-(mc:MarginCall {id:{id}})-[:LAST]->(step:Step) " +
                "WITH mc, m, p " +
                "MATCH a=(m)-[:SENT_FROM|DIRECTED_TO]->()-[:ACCESSES]->() " +
                "RETURN mc, nodes(p), relationships(p), nodes(a), relationships(a)";
        return dao.getSession().queryForObject(MarginCall.class, query, ImmutableMap.of("id", callId));
    }

    @Override
    @Transactional
    public Iterable<MarginCall> calls(String... callIds) {
        String query =
                "MATCH p=(firm:Firm)-[:MANAGES]->(l:LegalEntity)-[r:CLIENT_SIGNS|COUNTERPARTY_SIGNS]->" +
                "(a:Agreement)<-[]-(m:MarginStatement)<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "WHERE mc.id IN {ids} " +
                "RETURN mc, nodes(p), relationships(p)";
        return dao.getSession().query(MarginCall.class, query, ImmutableMap.of("ids", callIds));
    }

    @Override
    @Deprecated
    @Transactional
    public Iterable<MarginCall> callsForExpected(String marginStatementId) {
        String query =
                "MATCH (:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m:MarginStatement {id:{msId}})" +
                "<-[]-(mc1:MarginCall)-[:LAST]->(step:Step {status:'Unrecon'}) " +
                "MATCH (mc2:MarginCall)<-[:MATCHED_TO]-(mc1) " +
                "return mc2";
        return dao.getSession().query(MarginCall.class, query, ImmutableMap.of("msId", marginStatementId));
    }

    @Override
    @Transactional
    public Iterable<MarginCall> allExpectedCallsFor(MarginStatementId marginStatementId) {
        String query =
                "MATCH (m:MarginStatement {id:{msId}})<-[]-(mc:MarginCall)-[:LAST]->(step:Step {status:'Expected'}) " +
                "return mc";
        ImmutableMap<String, String> parameters = ImmutableMap.of("msId", marginStatementId.toString());
        return dao.getSession().query(MarginCall.class, query, parameters);
    }

    @Override
    @Transactional
    public Iterable<Call> notToUseYetallCallsFor(String clientId, String dateTime) {
        String query =
                "MATCH (:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[r:CLIENT_SIGNS]->(a:Agreement)" +
                "<-[:STEMS_FROM]-(m:MarginStatement {date:{dateTime}}) " +
                "WITH a, m " +
                "MATCH (m)<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "WITH {category:a.type, type:mc.callType, status:step.status, balance: mc.balanceAmount, excess: mc.excessAmount} AS Call " +
                "RETURN Call";
        ImmutableMap<String, String> parameters = ImmutableMap.of("clientId", clientId, "dateTime", dateTime);
        return dao.getSession().query(Call.class, query, parameters);
    }

    private static final BiPredicate<MarginCall, MarginCall> MATCHING_CALLS =
            (clientCall, cptyCall) ->
                    clientCall.getCallDate().equals(cptyCall.getCallDate()) &&
                            clientCall.getMarginType().equals(cptyCall.getMarginType());

    @Override
    @Transactional
    public MarginCall matchToExpected(String callId) {
        MarginCall cptyCall = find(callId, 1);
        StatementItem matchedItem = cptyCall.getMatchedItem();
        if (matchedItem == null) {
            final MarginStatementId marginStatementId = MarginStatementId.fromString(cptyCall.getMarginStatement().getStatementId());
            final Iterable<MarginCall> marginCalls = allExpectedCallsFor(marginStatementId);

            MarginCall finalCptyCall = cptyCall;
            final Optional<MarginCall> expected = StreamSupport.stream(marginCalls.spliterator(), false)
                    .filter(clientCall -> MATCHING_CALLS.test(clientCall, finalCptyCall))
                    .findFirst();
            if (expected.isPresent()) {
                cptyCall.setMatchedItem(expected.get());
                cptyCall = save(cptyCall);

                MarginCall clientMC = expected.get();
                clientMC.setAmpId(cptyCall.getAmpId());
                clientMC = save(clientMC);

                statementItemService.setStatus(clientMC.getItemId(), StatementStatus.MatchedToReceived);
            }
            cptyCall = statementItemService.setStatus(cptyCall.getItemId(), StatementStatus.Unrecon);
        }
        return cptyCall;
    }

    @Override
    @Transactional
    public MarginCall matchToSent(String callId) {
        MarginCall cptyCall = find(callId, 1);
        StatementItem matchedItem = cptyCall.getMatchedItem();
        if (matchedItem == null) {
            final MarginStatementId marginStatementId = MarginStatementId.fromString(cptyCall.getMarginStatement().getStatementId());
            final Iterable<MarginCall> marginCalls = allExpectedCallsFor(marginStatementId);

            MarginCall finalCptyCall = cptyCall;
            final Optional<MarginCall> expected = StreamSupport.stream(marginCalls.spliterator(), false)
                    .filter(clientCall -> MATCHING_CALLS.test(clientCall, finalCptyCall))
                    .findFirst();
            if (expected.isPresent()) {
                cptyCall.setMatchedItem(expected.get());
                cptyCall = save(cptyCall);

                MarginCall clientMC = expected.get();
                clientMC.setAmpId(cptyCall.getAmpId());
                clientMC = save(clientMC);

                statementItemService.setStatus(clientMC.getItemId(), StatementStatus.Reconciled);
            }
            cptyCall = statementItemService.setStatus(cptyCall.getItemId(), StatementStatus.MatchedToSent);
        }
        return cptyCall;
    }

    @Override
    @Transactional
    public List<com.acuo.common.model.margin.MarginCall> getDisputeMarginCall(String marginStatementId) {
        List<com.acuo.common.model.margin.MarginCall> marginCalls = new ArrayList<>();
        String query = "MATCH (ms:MarginStatement {id:{id}})<-[:ON]-(d:Dispute) " +
                "MATCH (mc:MarginCall {side:'Cpty'})-[:PART_OF]->(ms) " +
                "RETURN mc.ampId, d.agreedAmount, d.disputeReasonCodes, d.comments, d.mtm, d.balance ";

        Result result = dao.getSession().query(query, ImmutableMap.of("id", marginStatementId));
        result.forEach(map -> marginCalls.add(buildDisputeMarginCall(map)));
        return marginCalls;
    }

    private com.acuo.common.model.margin.MarginCall buildDisputeMarginCall(Map<String, Object> map) {
        com.acuo.common.model.margin.MarginCall marginCall = new com.acuo.common.model.margin.MarginCall();
        marginCall.setAmpId((String) map.get("mc.ampId"));
        //marginCall.setVersion(((Long)map.get("mc.version")).intValue());

        //marginCall.setAgreedAmount((Double) map.get("d.agreedAmount"));
        Dispute dispute = new Dispute();
        marginCall.setDispute(dispute);
        Set<Types.DisputeReasonCode> disputeReasonCodeSet = new HashSet<>();
        dispute.setDisputeReasonCodes(disputeReasonCodeSet);
        String[] codes = (String[]) map.get("d.disputeReasonCodes");
        Arrays.stream(codes).forEach(s -> disputeReasonCodeSet.add(Types.DisputeReasonCode.valueOf(s)));
        //disputeReasonCodeSet.add(Types.DisputeReasonCode.valueOf((map.get("d.disputeReasonCodes").toString())));
        dispute.setComments((String) map.get("d.comments"));
        dispute.setMtm((Double) map.get("d.mtm"));
        dispute.setAgreedAmount((Double) map.get("d.agreedAmount"));
        dispute.setBalance((Double) map.get("d.balance"));

        return marginCall;
    }

    @Override
    @Transactional
    public com.acuo.common.model.margin.MarginCall getPledgeMarginCall(String marginCallId) {
        com.acuo.common.model.margin.MarginCall marginCall = new com.acuo.common.model.margin.MarginCall();
        String query =
                "MATCH (mc:MarginCall {id:{id}})<-[:GENERATED_BY]-(at:AssetTransfer)-[:OF]->(a:Asset) " +
                "MATCH (mc)-[PART_OF]->(:MarginStatement)-[:STEMS_FROM]->(:Agreement)-[IS_COMPOSED_OF]->" +
                "(r:Rule)-[:APPLIES_TO]->(a) " +
                "MATCH (ca:CustodianAccount)<-[:FROM]-(at) " +
                "RETURN mc.ampId, a.id, a.idType, a.currency, at.quantity, at.value, r.haircut + r.FXHaircut as haircut";

        ImmutableMap<String, String> parameters = ImmutableMap.of("id", marginCallId);
        Result result = dao.getSession().query(query, parameters);
        Map<String, Object> map = result.iterator().next();
        //TODO: 2017/5/15 0015   check if Custodian account ca is owned by the client or the counterpart.
        //If it's owned by the client, then deliveryType is 'deliver', otherwise it's 'return'

        if (map != null) {
            marginCall.setAmpId((String) map.get("mc.ampId"));
            marginCall.setCurrency(Currency.of((String) map.get("a.currency")));

        }
        return marginCall;
    }

    @Override
    @Transactional
    public void sentMS(MarginCall marginCall) {
        marginCall.setSentMS(1);
        save(marginCall);
    }
}