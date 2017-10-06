package com.acuo.persist.builders

import com.acuo.persist.entity.Agreement
import com.acuo.persist.entity.MarginStatement
import com.acuo.persist.services.AgreementService

import javax.inject.Inject

class AgreementFactory extends AbstractFactory implements BuilderFactory {

    private final AgreementService service

    @Inject
    AgreementFactory(AgreementService service) {
        this.service = service
    }

    @Override
    String name() {
        return "agreement"
    }

    @Override
    boolean isLeaf() {
        return false
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder,
                       Object name, Object value, Map attributes
    ) throws InstantiationException, IllegalAccessException {
        def agreement
        if (attributes != null)
            agreement = getOrCreate(attributes)
        else
            agreement = new Agreement()
        return agreement
    }

    @Override
    void setParent(FactoryBuilderSupport builder,
                   Object parent, Object agreement) {
        if (parent != null && parent instanceof MarginStatement)
            parent.setAgreement(agreement as Agreement)
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object agreement) {
        service.save(agreement)
    }

    private Agreement getOrCreate(Map attributes) {
        Agreement agreement
        if (attributes != null) {
            String id = attributes["agreementId"]
            if (id != null) {
                agreement = service.find(id)
            }
            if (agreement == null) {
                return new Agreement(attributes)
            } else
                return agreement
        } else {
            agreement = new Agreement()
        }
        return agreement
    }
}
