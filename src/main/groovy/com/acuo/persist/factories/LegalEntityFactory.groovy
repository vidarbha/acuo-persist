package com.acuo.persist.factories

import com.acuo.persist.entity.Client
import com.acuo.persist.entity.ClientSignsRelation
import com.acuo.persist.entity.LegalEntity
import com.acuo.persist.services.LegalEntityService

import javax.inject.Inject

class LegalEntityFactory extends AbstractFactory implements BuilderFactory {

    private final LegalEntityService service

    @Inject
    LegalEntityFactory(LegalEntityService service) {
        this.service = service
    }

    @Override
    String name() {
        return "entity"
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder,
                       Object name,
                       Object value,
                       Map attributes) throws InstantiationException, IllegalAccessException {
        return getOrCreate(attributes)
    }

    @Override
    void setParent( FactoryBuilderSupport builder, Object parent, Object child ) {
        def entity = child as LegalEntity
        if (parent != null) {
            switch (parent) {
                case Client:
                    if (parent.legalEntities != null) {
                        parent.legalEntities << entity
                    } else {
                        parent.legalEntities = [ entity ]
                    }
                    break
                case ClientSignsRelation:
                    parent.legalEntity = entity
                    break
            }
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object agreement) {
        service.save(agreement)
    }

    private LegalEntity getOrCreate(Map attributes) {
        LegalEntity entity
        if (attributes != null) {
            String id = attributes["legalEntityId"]
            if (id != null) {
                entity = service.find(id)
            }
            if (entity == null) {
                return new LegalEntity(attributes)
            } else
                return entity
        } else {
            entity = new LegalEntity()
        }
        return entity
    }
}
