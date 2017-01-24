package com.acuo.persist.services;

import com.acuo.persist.entity.CallStatus;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.spring.Call;

public interface MarginCallService extends Service<MarginCall>  {

    void setStatus(String marginCallId, CallStatus status);

    Iterable<MarginCall> allCallsFor(String clientId, CallStatus... statuses);

    Iterable<MarginCall> callFor(String marginStatementId, CallStatus... statuses);

    @Deprecated
    Iterable<MarginCall> callsForExpected(String marginStatementId);

    Iterable<Call> notToUseYetallCallsFor(String clientId, String dateTime);
}
