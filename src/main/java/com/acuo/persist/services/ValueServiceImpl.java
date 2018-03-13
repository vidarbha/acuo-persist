package com.acuo.persist.services;

import com.acuo.persist.entity.Value;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class ValueServiceImpl extends AbstractService<Value, Long> implements  ValueService{

    @Inject
    public ValueServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<Value> getEntityType() {
        return Value.class;
    }

}
