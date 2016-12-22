package com.acuo.persist.services;

import com.acuo.persist.entity.MarginStatement;

public class MarginStatementServiceImpl extends GenericService<MarginStatement> implements MarginStatementService {

    @Override
    public Class<MarginStatement> getEntityType() {
        return MarginStatement.class;
    }
}
