package com.acuo.persist.factories

import com.acuo.persist.entity.Agreement
import com.acuo.persist.entity.AssetValuation
import com.acuo.persist.entity.Rule
import com.acuo.persist.services.AssetValuationService

import javax.inject.Inject

class AssetValuationFactory extends AbstractFactory implements BuilderFactory {

    private final AssetValuationService service

    @Inject
    AssetValuationFactory(AssetValuationService service) {
        this.service = service
    }

    @Override
    String name() {
        return "assetValuation"
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
        if (parent != null) {
            switch (parent) {
                case Agreement:
                    parent.rules << child as Rule
                    break
            }
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object child) {
        service.persist(child)
    }

    private static AssetValuation getOrCreate(Map attributes) {
        if (attributes != null) {
            return new AssetValuation(attributes)
        } else {
            return new AssetValuation()
        }
    }
}
