package com.acuo.persist.services;

import com.acuo.persist.entity.Agreement;
import com.acuo.persist.entity.MarginStatement;
import com.acuo.persist.entity.StatementItem;
import com.acuo.persist.entity.enums.StatementDirection;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.common.model.ids.ClientId;
import com.acuo.common.model.ids.MarginStatementId;

import java.time.LocalDate;

public interface MarginStatementService extends Service<MarginStatement, String> {

    Iterable<MarginStatement> allStatementsFor(ClientId clientId, StatementStatus... statuses);

    MarginStatement statementFor(MarginStatementId marginStatementId, StatementStatus... statuses);

    Iterable<MarginStatement> allStatementsForClient(ClientId clientId);

    Iterable<MarginStatement> allStatementsForRecon(ClientId clientId);

    Iterable<MarginStatement> allUnmatchedStatements(ClientId clientId);

    MarginStatement statementForRecon(MarginStatementId marginStatementId);

    MarginStatement statementOf(String callId);

    Iterable<MarginStatement> statementOf(String... callIds);

    void reconcile(MarginStatementId marginStatementId, Double amount);

    void match(MarginStatementId fromId, MarginStatementId toId);

    MarginStatement getMarginStatement(Agreement agreement, LocalDate callDate/*, StatementDirection direction*/);

    MarginStatement getOrCreateMarginStatement(Agreement agreement, LocalDate callDate/*, StatementDirection direction*/);

    /**
     * @deprecated  {@link StatementItemService#setStatus(String, StatementStatus)}
     */
    @Deprecated
    <T extends StatementItem> T setStatus(String statementItemId, StatementStatus status);

    Long count(StatementStatus status/*, StatementDirection... directions*/);
}