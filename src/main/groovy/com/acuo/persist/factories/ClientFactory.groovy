package com.acuo.persist.factories

import com.acuo.persist.entity.Client
import com.acuo.persist.services.ClientService

import javax.inject.Inject

class ClientFactory extends AbstractFactory implements BuilderFactory {

    private final ClientService service

    @Inject
    ClientFactory(ClientService service) {
        this.service = service
    }

    @Override
    String name() {
        return "client"
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder,
                       Object name,
                       Object value,
                       Map attributes) throws InstantiationException, IllegalAccessException {
        return getOrCreate(attributes)
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object agreement) {
        service.save(agreement)
    }

    private Client getOrCreate(Map attributes) {
        Client client
        if (attributes != null) {
            String id = attributes["firmId"]
            if (id != null) {
                client = service.find(id)
            }
            if (client == null) {
                return new Client(attributes)
            } else
                return client
        } else {
            client = new Client()
        }
        return client
    }
}
