package com.acuo.persist.factories

import com.acuo.persist.entity.Asset
import com.acuo.persist.entity.Holds
import com.acuo.persist.services.HoldsService

import javax.inject.Inject

class HoldsFactory extends AbstractFactory implements BuilderFactory {

    private final HoldsService service

    @Inject
    HoldsFactory(HoldsService service) {
        this.service = service
    }

    @Override
    String name() {
        return "holds"
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
        def holds = child as Holds
        if (parent != null) {
            switch (parent) {
                case Asset:
                    holds.asset = parent
                    parent.holds = holds
                    break
            }
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object entity) {
        service.save(entity)
    }

    private static Holds getOrCreate(Map attributes) {
        Holds entity
        if (attributes != null) {
            return new Holds(attributes)
        } else {
            entity = new Holds()
        }
        return entity
    }
}
