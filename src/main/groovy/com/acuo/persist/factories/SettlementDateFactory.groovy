package com.acuo.persist.factories

import com.acuo.persist.entity.Settlement
import com.acuo.persist.entity.SettlementDate
import com.acuo.persist.services.SettlementDateService

import javax.inject.Inject

class SettlementDateFactory extends AbstractFactory implements BuilderFactory {

    private final SettlementDateService service

    @Inject
    SettlementDateFactory(SettlementDateService service) {
        this.service = service
    }

    @Override
    String name() {
        return "settlementDate"
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
        def settlementDate = child as SettlementDate
        if (parent != null) {
            switch (parent) {
                case Settlement:
                        parent.latestSettlementDate = settlementDate
                    if (parent.settlementDates != null) {
                        parent.settlementDates << settlementDate
                    } else {
                        parent.settlementDates = [settlementDate]
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

    private static SettlementDate getOrCreate(Map attributes) {
        SettlementDate settlementDate
        if (attributes != null) {
            return new SettlementDate(attributes)
        } else {
            settlementDate = new SettlementDate()
        }
        return settlementDate
    }
}
