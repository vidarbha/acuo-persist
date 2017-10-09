package com.acuo.persist.factories

import com.acuo.persist.entity.Collateral
import com.acuo.persist.entity.MarginStatement
import com.acuo.persist.services.CollateralService

import javax.inject.Inject

class CollateralFactory extends AbstractFactory implements BuilderFactory {

    private final CollateralService service

    @Inject
    CollateralFactory(CollateralService service) {
        this.service = service
    }

    @Override
    String name() {
        return "collateral"
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder,
                       Object name, Object value, Map attributes
    ) throws InstantiationException, IllegalAccessException {
        return getOrCreate(attributes)
    }

    @Override
    void setParent(FactoryBuilderSupport builder,
                   Object parent, Object child) {
        def entity = child as Collateral
        if (parent != null) {
            switch (parent) {
                case MarginStatement:
                    parent.collaterals << entity
                    break
            }
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object agreement) {
        service.save(agreement)
    }

    private Collateral getOrCreate(Map attributes) {
        Collateral collateral
        if (attributes != null) {
            return new Collateral(attributes)
        } else {
            collateral = new Collateral()
        }
        return collateral
    }
}
