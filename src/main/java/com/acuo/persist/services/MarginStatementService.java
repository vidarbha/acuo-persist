package com.acuo.persist.services;

import com.acuo.persist.entity.CallStatus;
import com.acuo.persist.entity.MarginStatement;
import com.acuo.persist.ids.ClientId;
import com.acuo.persist.ids.MarginStatementId;

public interface MarginStatementService extends Service<MarginStatement> {

    Iterable<MarginStatement> allStatementsFor(ClientId clientId, CallStatus... statuses);

    MarginStatement statementFor(MarginStatementId marginStatementId, CallStatus... statuses);

    Iterable<MarginStatement> allStatementsForClient(ClientId clientId);

    Iterable<MarginStatement> allStatementsForRecon(ClientId clientId);

    MarginStatement statementForRecon(MarginStatementId marginStatementId);

    MarginStatement statementOf(String callId);
}