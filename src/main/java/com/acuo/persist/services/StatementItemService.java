package com.acuo.persist.services;

import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.entity.StatementItem;

public interface StatementItemService extends Service<StatementItem> {

    void setStatus(StatementItem statementItem, StatementStatus status);
}
