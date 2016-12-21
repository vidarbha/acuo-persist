package com.acuo.persist.services;

import com.acuo.persist.entity.Client;

public class ClientServiceImpl extends GenericService<Client> implements ClientService{

    @Override
    public Class<Client> getEntityType() {
        return Client.class;
    }
}
