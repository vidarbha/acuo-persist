package com.acuo.persist.factories

import com.acuo.persist.entity.MarginStatement
import com.acuo.persist.entity.VariationMargin
import com.acuo.persist.services.MarginCallService

import javax.inject.Inject

class VariationMarginFactory extends AbstractFactory implements BuilderFactory {

    MarginCallService service

    @Inject
    VariationMarginFactory(MarginCallService service) {
        this.service = service
    }

    @Override
    String name() {
        return "variation"
    }

    @Override
    boolean isLeaf() {
        return false
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
                   Object parent,
                   Object call) {
        if (parent != null && parent instanceof MarginStatement) {
            def margin = call as VariationMargin
            if (parent.statementItems != null) {
                parent.statementItems << margin
            } else {
                parent.statementItems = [margin]
            }
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent,
                         Object agreement) {
        service.save(agreement)
    }

    private VariationMargin getOrCreate(Map attributes) {
        VariationMargin call
        if (attributes != null) {
            String id = attributes["itemId"]
            if (id != null) {
                call = service.find(id) as VariationMargin
            }
            if (call == null) {
                return new VariationMargin(attributes)
            } else
                return call
        } else {
            call = new VariationMargin()
        }
        return call
    }
}
