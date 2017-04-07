package com.acuo.persist.services;

import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.Next;
import com.acuo.persist.entity.Step;
import com.acuo.persist.spring.Call;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;


public class MarginCallServiceImpl extends GenericService<MarginCall> implements MarginCallService {

    @Override
    public Class<MarginCall> getEntityType() {
        return MarginCall.class;
    }

    @Override
    @Transactional
    public void setStatus(String marginCallId, StatementStatus status) {
        MarginCall marginCall = findById(marginCallId);
        Step previousStep = marginCall.getLastStep();
        Step lastStep = new Step();
        Next next = new Next();
        next.setStart(previousStep);
        next.setEnd(lastStep);
        previousStep.setNext(next);
        lastStep.setStatus(status);
        marginCall.setLastStep(lastStep);
        save(marginCall);
    }

    @Override
    @Transactional
    public Iterable<MarginCall> allCallsFor(String clientId, StatementStatus... statuses) {
        String query =
                "MATCH p=(:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[r:CLIENT_SIGNS]->(a:Agreement)<-[:STEMS_FROM]-" +
                        "(m:MarginStatement)<-[*1..2]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                        "WHERE step.status IN {statuses} " +
                        "RETURN mc, nodes(p), rels(p)";
        return sessionProvider.get().query(MarginCall.class, query, ImmutableMap.of("clientId", clientId, "statuses", statuses));
    }

    @Override
    @Transactional
    public Iterable<MarginCall> callFor(String marginStatementId, StatementStatus... statuses) {
        String query =
                "MATCH p=(:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m:MarginStatement {id:{msId}})<-[]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                "WHERE step.status IN {statuses} " +
                "RETURN mc, nodes(p), rels(p)";
        return sessionProvider.get().query(MarginCall.class, query, ImmutableMap.of("msId", marginStatementId, "statuses", statuses));
    }

    @Override
    @Deprecated
    @Transactional
    public Iterable<MarginCall> callsForExpected(String marginStatementId) {
        String query = "MATCH (:Firm)-[:MANAGES]->(l:LegalEntity)-[]->(a:Agreement)<-[]-(m:MarginStatement {id:{msId}})<-[]-(mc1:MarginCall)-[:LAST]->(step:Step {status:'Unrecon'}) " +
                "MATCH (mc2:MarginCall)<-[MATCHED_TO_EXPECTED]-(mc1) " +
                "return mc2";
        return sessionProvider.get().query(MarginCall.class, query, ImmutableMap.of("msId", marginStatementId));
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
}