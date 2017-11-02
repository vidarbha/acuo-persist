package com.acuo.persist.factories

import com.acuo.persist.entity.Asset
import com.acuo.persist.entity.Settlement
import com.acuo.persist.services.SettlementService

import javax.inject.Inject

class SettlementFactory extends AbstractFactory implements BuilderFactory{

    private final SettlementService service

    @Inject
    SettlementFactory(SettlementService service) {
        this.service = service
    }

    @Override
    String name() {
        return "settlement"
    }

    @Override
    boolean isLeaf() {
        return false
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder,
                       Object name,
                       Object value,
                       Map attributes) throws InstantiationException, IllegalAccessException {
        return getOrCreate(attributes)
    }

    @Override
    void setParent( FactoryBuilderSupport builder, Object parent, Object child ) {
        def settlement = child as Settlement
        if (parent != null) {
            switch (parent) {
                case Asset:
                    parent.settlement = settlement
                    break
            }
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent,
                         Object settlement) {
        service.save(settlement)
    }

    private Settlement getOrCreate(Map attributes) {
        Settlement settlement
        if (attributes != null) {
            String id = attributes["settlementId"]
            if (id != null) {
                settlement = service.find(id)
            }
            if (settlement == null) {
                return new Settlement(attributes)
            } else
                return settlement
        } else {
            settlement = new Settlement()
        }
        return settlement
    }
}
