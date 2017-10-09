package com.acuo.persist.services;

import com.acuo.persist.entity.ClientSignsRelation;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class ClientSignsRelationServiceImpl extends AbstractService<ClientSignsRelation, Long>
        implements ClientSignsRelationService {

    @Inject
    public ClientSignsRelationServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<ClientSignsRelation> getEntityType() {
        return ClientSignsRelation.class;
    }
}
