package com.acuo.persist.services;

import com.acuo.common.model.margin.Dispute;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.common.model.ids.MarginStatementId;
import com.acuo.persist.spring.Call;

import java.util.List;

public interface MarginCallService extends Service<MarginCall, String> {

    Iterable<MarginCall> allCallsFor(String clientId, StatementStatus... statuses);

    Iterable<MarginCall> callFor(String marginStatementId, StatementStatus... statuses);

    MarginCall callByAmpId(String ampId);

    Iterable<MarginCall> calls(String... callIds);

    @Deprecated
    Iterable<MarginCall> callsForExpected(String marginStatementId);

    Iterable<Call> notToUseYetallCallsFor(String clientId, String dateTime);

    Iterable<MarginCall> allExpectedCallsFor(MarginStatementId marginStatementId);

    MarginCall matchToExpected(String callId);

    List<com.acuo.common.model.margin.MarginCall> getDisputeMarginCall(String marginStatementId);

    com.acuo.common.model.margin.MarginCall getPledgeMarginCall(String marginCallId);

    MarginCall createPartialDisputeCall(MarginCall parent, MarginCall child, Dispute dispute, StatementStatus status);

    void sentMS(MarginCall marginCall);

}
