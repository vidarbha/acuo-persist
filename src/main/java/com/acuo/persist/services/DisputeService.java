package com.acuo.persist.services;

import com.acuo.persist.entity.Dispute;
import com.acuo.persist.entity.MarginStatement;
import com.acuo.common.ids.MarginStatementId;

public interface DisputeService extends Service<Dispute, String> {

    Dispute relatedTo(MarginStatementId marginStatementId);

    Dispute add(MarginStatement statement, com.acuo.common.model.margin.Dispute dispute);
}