package com.acuo.persist.services;

import com.acuo.common.model.results.MSError;
import com.acuo.persist.entity.StatementItem;
import com.acuo.persist.entity.enums.StatementStatus;

public interface StatementItemService extends Service<StatementItem, String> {

    <T extends StatementItem> T setStatus(String statementItemId, StatementStatus status);

    <T extends StatementItem> T setStatus(String statementItemId, StatementStatus status, MSError msError);

}
