package com.acuo.persist.services;

import com.acuo.persist.entity.Agreement;
import com.acuo.persist.entity.LegalEntity;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class LegalEntityServiceImpl extends AbstractService<LegalEntity, String> implements LegalEntityService {

    @Inject
    public LegalEntityServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<LegalEntity> getEntityType() {
        return LegalEntity.class;
    }

    @Override
    @Transactional
    public LegalEntity getClientLegalEntity(Agreement agreement) {
        String query = "MATCH (l:LegalEntity)-[r:ClientSignsRelation]->(a:Agreement {id:{id}}) " +
                        "RETURN l";
        return dao.getSession().queryForObject(LegalEntity.class, query, ImmutableMap.of("id",agreement.getAgreementId()));
    }

    @Override
    @Transactional
    public LegalEntity getCtpyLegalEntity(Agreement agreement) {
        String query = "MATCH (l:LegalEntity)-[r:COUNTERPARTY_SIGNS]->(a:Agreement {id:{id}}) " +
                        "RETURN l";
        return dao.getSession().queryForObject(LegalEntity.class, query, ImmutableMap.of("id",agreement.getAgreementId()));
    }
}
