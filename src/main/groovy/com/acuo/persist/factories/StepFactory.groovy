package com.acuo.persist.factories

import com.acuo.persist.entity.Next
import com.acuo.persist.entity.StatementItem
import com.acuo.persist.entity.Step
import com.acuo.persist.services.StepService

import javax.inject.Inject

class StepFactory extends AbstractFactory implements BuilderFactory {

    private final StepService service

    @Inject
    StepFactory(StepService service) {
        this.service = service
    }

    @Override
    String name() {
        return "step"
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder,
                       Object name,
                       Object value,
                       Map attributes) throws InstantiationException, IllegalAccessException {
        return getOrCreate()
    }

    @Override
    void setParent( FactoryBuilderSupport builder, Object parent, Object child ) {
        def step = child as Step
        if (parent != null) {
            switch (parent) {
                case StatementItem:
                    if (parent.firstStep == null && parent.lastStep == null) {
                        parent.firstStep = parent.lastStep = step
                    } else if (parent.lastStep == null) {
                        parent.lastStep = step
                        Next next = new Next()
                        next.setStart parent.firstStep
                        next.setEnd parent.lastStep
                        parent.firstStep.next = next
                    }
                    break
            }
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object child) {
        service.save(child)
    }

    private static Step getOrCreate(Map attributes) {
        Step step
        if (attributes != null) {
            return new Step(attributes)
        } else {
            step = new Step()
        }
        return step
    }
}
