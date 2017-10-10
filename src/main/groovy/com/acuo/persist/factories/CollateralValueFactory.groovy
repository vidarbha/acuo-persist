package com.acuo.persist.factories

import com.acuo.persist.entity.Collateral
import com.acuo.persist.entity.CollateralValue
import com.acuo.persist.services.CollateralValueService

import javax.inject.Inject

class CollateralValueFactory extends AbstractFactory implements BuilderFactory {

    private final CollateralValueService service

    @Inject
    CollateralValueFactory(CollateralValueService service) {
        this.service = service
    }

    @Override
    String name() {
        return "collateralValue"
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder,
                       Object name,
                       Object value,
                       Map attributes
    ) throws InstantiationException, IllegalAccessException {
        return getOrCreate(attributes)
    }

    @Override
    void setParent(FactoryBuilderSupport builder,
                   Object parent, Object child) {
        def entity = child as CollateralValue
        if (parent != null) {
            switch (parent) {
                case Collateral:
                    parent.latestValue = entity
                    parent.values << entity
                    break
            }
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object child) {
        service.save(child)
    }

    private static CollateralValue getOrCreate(Map attributes) {
        CollateralValue collateral
        if (attributes != null) {
            return new CollateralValue(attributes)
        } else {
            collateral = new CollateralValue()
        }
        return collateral
    }
}
