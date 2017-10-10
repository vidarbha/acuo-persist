package com.acuo.persist.services;

import com.acuo.persist.entity.Custodian;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class CustodianServiceImpl extends AbstractService<Custodian, String> implements CustodianService {

    @Inject
    public CustodianServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<Custodian> getEntityType() {
        return Custodian.class;
    }
}
