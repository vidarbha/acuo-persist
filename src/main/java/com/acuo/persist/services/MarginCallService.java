package com.acuo.persist.services;

import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.spring.Call;

public interface MarginCallService extends Service<MarginCall, String> {

    Iterable<MarginCall> allCallsFor(String clientId, StatementStatus... statuses);

    Iterable<MarginCall> callFor(String marginStatementId, StatementStatus... statuses);

    @Deprecated
    Iterable<MarginCall> callsForExpected(String marginStatementId);

    Iterable<Call> notToUseYetallCallsFor(String clientId, String dateTime);
}
