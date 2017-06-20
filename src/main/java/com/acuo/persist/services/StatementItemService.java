package com.acuo.persist.services;

import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.entity.StatementItem;

public interface StatementItemService extends Service<StatementItem, String> {

    <T extends StatementItem> T setStatus(String statementItemId, StatementStatus status);
}
