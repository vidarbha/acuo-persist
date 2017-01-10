package com.acuo.persist.services;

import com.acuo.persist.entity.*;
import com.google.common.collect.ImmutableMap;

public class MarginCallServiceImpl extends GenericService<MarginCall> implements MarginCallService {

    @Override
    public Class<MarginCall> getEntityType() {
        return MarginCall.class;
    }

    @Override
    public void setStatus(String marginCallId, CallStatus status)
    {
        MarginCall marginCall = this.findById(marginCallId, 1);
        Step previousStep = marginCall.getLastStep();
        Step lastStep = new Step();
        Next next = new Next();
        next.setStart(previousStep);
        next.setEnd(lastStep);
        previousStep.setNext(next);
        lastStep.setStatus(status);
        marginCall.setLastStep(lastStep);
        this.createOrUpdate(marginCall);
    }

    @Override
    public Iterable<MarginCall> allCallsFor(String clientId, CallStatus... statuses) {
        String query =
                "MATCH p=(:Client {id:{clientId}})-[:MANAGES]->(l:LegalEntity)-[r:CLIENT_SIGNS]->(a:Agreement)<-[:STEMS_FROM]-" +
                        "(m:MarginStatement)<-[*1..2]-(mc:MarginCall)-[:LAST]->(step:Step) " +
                        "WHERE step.status IN {statuses} " +
                        "RETURN mc, nodes(p), rels(p)";
        return session.query(MarginCall.class, query, ImmutableMap.of("clientId", clientId, "statuses", statuses));
    }

    @Override
    public MarginStatement statementOf(String callId) {
        String query = "MATCH p=(a:Agreement)<-[:STEMS_FROM]-(m:MarginStatement)<-[*1..2]-(mc:MarginCall {id:{callId}}) RETURN m, nodes(p), rels(p)";
        return session.queryForObject(MarginStatement.class, query, ImmutableMap.of("callId", callId));
    }
}
