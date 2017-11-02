package com.acuo.persist.factories

import com.acuo.persist.entity.Agreement
import com.acuo.persist.entity.Rule
import com.acuo.persist.services.RuleService

import javax.inject.Inject

class RuleFactory extends AbstractFactory implements BuilderFactory {

    private final RuleService service

    @Inject
    RuleFactory(RuleService service) {
        this.service = service
    }

    @Override
    String name() {
        return "rule"
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

    private static Rule getOrCreate(Map attributes) {
        Rule rule
        if (attributes != null) {
            return new Rule(attributes)
        } else {
            rule = new Rule()
        }
        return rule
    }
}
