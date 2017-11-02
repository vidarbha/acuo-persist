package com.acuo.persist.factories

import com.acuo.persist.entity.Custodian
import com.acuo.persist.entity.CustodianAccount
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
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        Custodian custodian = child as Custodian
        if (parent != null) {
            switch (parent) {
                case CustodianAccount:
                    parent.custodian = custodian
                    break
            }
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object child) {
        service.save(child)
    }

    private Custodian getOrCreate(Map attributes) {
        Custodian custodian
        if (attributes != null) {
            String id = attributes["custodianId"]
            if (id != null) {
                custodian = service.find(id)
            }
            if (custodian == null) {
                return new Custodian(attributes)
            } else {
                return custodian
            }
        } else {
            custodian = new Custodian()
        }
        return custodian
    }
}
