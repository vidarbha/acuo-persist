package com.acuo.persist.services;

import com.acuo.persist.entity.MarginCall;

public class MarginCallServiceImpl extends GenericService<MarginCall> implements MarginCallService {

    @Override
    public Class<MarginCall> getEntityType() {
        return MarginCall.class;
    }
}
