package com.acuo.persist.factories

import com.acuo.common.model.ids.AssetId
import com.acuo.persist.entity.Agreement
import com.acuo.persist.entity.Asset
import com.acuo.persist.entity.Custodian
import com.acuo.persist.entity.Rule
import com.acuo.persist.services.CustodianService

import javax.inject.Inject

class CustodianFactory extends AbstractFactory implements BuilderFactory {

    private final CustodianService service

    @Inject
    CustodianFactory(CustodianService service) {
        this.service = service
    }

    @Override
    String name() {
        return "custodian"
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
        service.save(child)
    }

    private Asset getOrCreate(Map attributes) {
        Custodian custodian
        if (attributes != null) {
            String id = attributes["custodianId"]
            if (id != null) {
                return service.find(AssetId.fromString(id))
            } else {
                return new Custodian(attributes)
            }
        } else {
            custodian = new Custodian()
        }
        return custodian
    }
}