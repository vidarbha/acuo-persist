package com.acuo.persist.services;

import com.acuo.persist.entity.SettlementDate;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class SettlementDateServiceImpl extends AbstractService<SettlementDate, String> implements SettlementDateService {

    @Inject
    public SettlementDateServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<SettlementDate> getEntityType() {
        return SettlementDate.class;
    }
}