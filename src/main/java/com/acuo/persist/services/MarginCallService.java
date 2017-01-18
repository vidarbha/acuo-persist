package com.acuo.persist.services;

import com.acuo.persist.entity.CallStatus;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.MarginStatement;

public interface MarginCallService extends Service<MarginCall>  {

    void setStatus(String marginCallId, CallStatus status);

    Iterable<MarginCall> allCallsFor(String clientId, CallStatus... statuses);

    Iterable<MarginCall> statementForExpected(String marginStatementId);

    Iterable<MarginCall> allCallForReconciled(String clientId);
}
