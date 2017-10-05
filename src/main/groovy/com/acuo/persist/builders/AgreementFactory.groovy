package com.acuo.persist.builders

import com.acuo.persist.entity.Agreement
import com.acuo.persist.entity.MarginStatement

class AgreementFactory extends AbstractFactory implements BuilderFactory {

    boolean isLeaf() {
        return false
    }

    Object newInstance(FactoryBuilderSupport builder,
                       Object name, Object value, Map attributes
    ) throws InstantiationException, IllegalAccessException {
        Agreement agreement = null
        if (attributes != null)
            agreement = new Agreement(attributes)
        else
            agreement = new Agreement()
        if (value != null && value instanceof MarginStatement)
            value.setAgreement(agreement)
        return agreement
    }

    void setParent(FactoryBuilderSupport builder,
                   Object parent, Object invoice) {
        if (parent != null && parent instanceof MarginStatement)
            parent.setAgreement(invoice)
    }

    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object invoice) {
        //agreement.save()
    }

    @Override
    String name() {
        return "agreement"
    }
}
