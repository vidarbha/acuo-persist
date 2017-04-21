package com.acuo.persist.services;

import com.acuo.persist.entity.Counterpart;
import com.acuo.persist.entity.LegalEntity;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

public class CounterpartServiceImpl extends GenericService<Counterpart, String> implements CounterpartService {

    @Override
    public Class<Counterpart> getEntityType() {
        return Counterpart.class;
    }

    @Override
    @Transactional
    public Counterpart getCounterpart(LegalEntity legalEntity) {
        String query = "MATCH (cp:Counterpart)-[r:MANAGES]->(l:LegalEntity {id:{id}}) " +
                        "RETURN cp";
        return  sessionProvider.get().queryForObject(Counterpart.class, query, ImmutableMap.of("id",legalEntity.getLegalEntityId()));
    }
}
