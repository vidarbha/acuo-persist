package com.acuo.persist.services;

import com.acuo.persist.entity.FXValue;

public class FXValueServiceImpl extends GenericService<FXValue, Long> implements FXValueService {

    @Override
    public Class<FXValue> getEntityType() {
        return FXValue.class;
    }
}
