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

    boolean isLeaf() {
        return false
    }

    Object newInstance(FactoryBuilderSupport builder,
                       Object name, Object value, Map attributes
    ) throws InstantiationException, IllegalAccessException {
        def agreement
        if (attributes != null)
            agreement = new Agreement(attributes)
        else
            agreement = new Agreement()
        if (value != null && value instanceof MarginStatement)
            value.setAgreement(agreement)
        return agreement
    }

    void setParent(FactoryBuilderSupport builder,
                   Object parent, Object agreement) {
        if (parent != null && parent instanceof MarginStatement)
            parent.setAgreement(agreement as Agreement)
    }

    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object agreement) {
        service.save(agreement)
    }

    @Override
    String name() {
        return "agreement"
    }
}
