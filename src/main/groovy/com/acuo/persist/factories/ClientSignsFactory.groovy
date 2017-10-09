package com.acuo.persist.factories

import com.acuo.persist.entity.ClientSignsRelation
import com.acuo.persist.services.ClientSignsRelationService

import javax.inject.Inject

class ClientSignsFactory extends AbstractFactory implements BuilderFactory {

    private final ClientSignsRelationService service

    @Inject
    ClientSignsFactory(ClientSignsRelationService service) {
        this.service = service
    }

    @Override
    String name() {
        return "clientSigns"
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
                         Object parent, Object clientSigns) {
        service.save(clientSigns)
    }

    private ClientSignsRelation getOrCreate(Map attributes) {
        ClientSignsRelation entity
        if (attributes != null) {
            return new ClientSignsRelation(attributes)
        } else {
            entity = new ClientSignsRelation()
        }
        return entity
    }
}
