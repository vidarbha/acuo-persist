package com.acuo.persist.services;

import com.acuo.common.model.ids.MarginStatementId;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.enums.Side;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.spring.Call;

import java.util.List;

public interface MarginCallService extends Service<MarginCall, String> {

    Iterable<MarginCall> allCallsFor(String clientId, StatementStatus... statuses);

    Iterable<MarginCall> callFor(String marginStatementId, StatementStatus... statuses);

    @Deprecated
    MarginCall callByAmpId(String ampId);

    MarginCall callByAmpIdAndSide(String ampId, Side side);

    MarginCall callById(String callId);

    Iterable<MarginCall> calls(String... callIds);

    @Deprecated
    Iterable<MarginCall> callsForExpected(String marginStatementId);

    Iterable<Call> notToUseYetallCallsFor(String clientId, String dateTime);

    Iterable<MarginCall> allExpectedCallsFor(MarginStatementId marginStatementId);

    MarginCall matchToExpected(String callId);

    MarginCall matchToSent(String callId);

    List<com.acuo.common.model.margin.MarginCall> getDisputeMarginCall(String marginStatementId);

    com.acuo.common.model.margin.MarginCall getPledgeMarginCall(String marginCallId);

    MarginCall link(MarginCall parent, MarginCall child);

    void sentMS(MarginCall marginCall);

}
