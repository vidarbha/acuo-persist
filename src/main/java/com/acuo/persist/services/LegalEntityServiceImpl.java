package com.acuo.persist.services;

import com.acuo.persist.entity.Agreement;
import com.acuo.persist.entity.LegalEntity;
import com.google.common.collect.ImmutableMap;

public class LegalEntityServiceImpl extends GenericService<LegalEntity> implements LegalEntityService {

    @Override
    public Class<LegalEntity> getEntityType() {
        return LegalEntity.class;
    }

    public LegalEntity getLegalEntity(Agreement agreement)
    {
        return  session.queryForObject(LegalEntity.class, "match (l:LegalEntity)-[r:CLIENT_SIGNS]->(a:Agreement {id:{id}}) return l", ImmutableMap.of("id",agreement.getAgreementId()) );
    }
}
