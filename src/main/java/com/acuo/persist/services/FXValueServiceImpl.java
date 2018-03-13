package com.acuo.persist.services;

import com.acuo.persist.entity.FXValue;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class FXValueServiceImpl extends AbstractService<FXValue, Long> implements FXValueService {

    @Inject
    public FXValueServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<FXValue> getEntityType() {
        return FXValue.class;
    }
}
