package com.acuo.persist.services;

import com.acuo.persist.entity.Client;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class ClientServiceImpl extends AbstractService<Client, String> implements ClientService{

    @Inject
    public ClientServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<Client> getEntityType() {
        return Client.class;
    }
}
