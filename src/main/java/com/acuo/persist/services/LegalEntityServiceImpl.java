package com.acuo.persist.services;

import com.acuo.persist.entity.Agreement;
import com.acuo.persist.entity.LegalEntity;
import com.google.common.collect.ImmutableMap;

public class LegalEntityServiceImpl extends GenericService<LegalEntity> implements LegalEntityService {

    @Override
    public Class<LegalEntity> getEntityType() {
        return LegalEntity.class;
    }

    public LegalEntity getClientLegalEntity(Agreement agreement)
    {
        return  session.queryForObject(LegalEntity.class, "match (l:LegalEntity)-[r:ClientSignsRelation]->(a:Agreement {id:{id}}) return l", ImmutableMap.of("id",agreement.getAgreementId()) );
    }

    public LegalEntity getCtpyLegalEntity(Agreement agreement)
    {
        return  session.queryForObject(LegalEntity.class, "match (l:LegalEntity)-[r:COUNTERPARTY_SIGNS]->(a:Agreement {id:{id}}) return l", ImmutableMap.of("id",agreement.getAgreementId()) );
    }

}
