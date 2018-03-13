package com.acuo.persist.services;

import com.acuo.persist.entity.Counterpart;
import com.acuo.persist.entity.LegalEntity;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Provider;

public class CounterpartServiceImpl extends AbstractService<Counterpart, String> implements CounterpartService {

    @Inject
    public CounterpartServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<Counterpart> getEntityType() {
        return Counterpart.class;
    }

    @Override
    @Transactional
    public Counterpart getCounterpart(LegalEntity legalEntity) {
        String query = "MATCH (cp:Counterpart)-[r:MANAGES]->(l:LegalEntity {id:{id}}) " +
                        "RETURN cp";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", legalEntity.getLegalEntityId());
        return  dao.getSession().queryForObject(Counterpart.class, query, parameters);
    }
}
