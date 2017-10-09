package com.acuo.persist.factories

import com.acuo.persist.entity.CounterpartSignsRelation
import com.acuo.persist.services.CounterpartSignsRelationService

import javax.inject.Inject

class CounterpartySignsFactory extends AbstractFactory implements BuilderFactory {

    private final CounterpartSignsRelationService service

    @Inject
    CounterpartySignsFactory(CounterpartSignsRelationService service) {
        this.service = service
    }

    @Override
    String name() {
        return "counterpartSigns"
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
                         Object parent, Object child) {
        service.save(child)
    }

    private CounterpartSignsRelation getOrCreate(Map attributes) {
        CounterpartSignsRelation entity
        if (attributes != null) {
            return new CounterpartSignsRelation(attributes)
        } else {
            entity = new CounterpartSignsRelation()
        }
        return entity
    }
}
