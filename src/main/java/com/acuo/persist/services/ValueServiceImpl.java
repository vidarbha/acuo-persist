package com.acuo.persist.services;

import com.acuo.persist.entity.Value;

public class ValueServiceImpl extends GenericService<Value> implements  ValueService{

    @Override
    public Class<Value> getEntityType() {
        return Value.class;
    }

}
