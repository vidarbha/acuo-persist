package com.acuo.persist.services;

import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.entity.StatementItem;

public interface StatementItemService extends Service<StatementItem, String> {

    void  setStatus(String statementItemId, StatementStatus status);
}
