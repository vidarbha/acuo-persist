package com.acuo.persist.services;

import com.acuo.persist.entity.Agreement;
import com.acuo.persist.entity.LegalEntity;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

public class LegalEntityServiceImpl extends GenericService<LegalEntity> implements LegalEntityService {

    @Override
    public Class<LegalEntity> getEntityType() {
        return LegalEntity.class;
    }

    @Override
    @Transactional
    public LegalEntity getClientLegalEntity(Agreement agreement) {
        String query = "MATCH (l:LegalEntity)-[r:ClientSignsRelation]->(a:Agreement {id:{id}}) " +
                        "RETURN l";
        return  sessionProvider.get().queryForObject(LegalEntity.class, query, ImmutableMap.of("id",agreement.getAgreementId()));
    }

    @Override
    @Transactional
    public LegalEntity getCtpyLegalEntity(Agreement agreement) {
        String query = "MATCH (l:LegalEntity)-[r:COUNTERPARTY_SIGNS]->(a:Agreement {id:{id}}) " +
                        "RETURN l";
        return  sessionProvider.get().queryForObject(LegalEntity.class, query, ImmutableMap.of("id",agreement.getAgreementId()));
    }

}
