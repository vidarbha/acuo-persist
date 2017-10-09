package com.acuo.persist.services;

import com.acuo.persist.entity.CounterpartSignsRelation;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class CounterpartSignsRelationServiceImpl extends AbstractService<CounterpartSignsRelation, Long>
        implements CounterpartSignsRelationService {

    @Inject
    public CounterpartSignsRelationServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<CounterpartSignsRelation> getEntityType() {
        return CounterpartSignsRelation.class;
    }
}
