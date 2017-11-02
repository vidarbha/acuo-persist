package com.acuo.persist.services;

import com.acuo.persist.entity.Holds;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class HoldsServiceImpl extends AbstractService<Holds, Long>
        implements HoldsService {

    @Inject
    public HoldsServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<Holds> getEntityType() {
        return Holds.class;
    }
}
