package com.acuo.persist.services;

import com.acuo.persist.entity.Counterpart;
import com.acuo.persist.entity.LegalEntity;
import com.google.common.collect.ImmutableMap;

public class CounterpartServiceImpl extends GenericService<Counterpart> implements CounterpartService {
    @Override
    public Class<Counterpart> getEntityType() {
        return Counterpart.class;
    }

    public Counterpart getCounterpart(LegalEntity legalEntity)
    {
        return  session.queryForObject(Counterpart.class, "match (cp:Counterpart)-[r:manages]->(l:LegalEntity {id:{id}}) return cp", ImmutableMap.of("id",legalEntity.getLegalEntityId()) );
    }
}
