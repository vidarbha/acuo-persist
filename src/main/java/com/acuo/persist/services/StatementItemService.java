package com.acuo.persist.services;

import com.acuo.persist.entity.CallStatus;
import com.acuo.persist.entity.StatementItem;

public interface StatementItemService extends Service<StatementItem> {

    void setStatus(StatementItem statementItem, CallStatus status);
}
