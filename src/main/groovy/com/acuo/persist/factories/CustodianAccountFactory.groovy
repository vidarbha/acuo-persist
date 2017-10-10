package com.acuo.persist.factories

import com.acuo.persist.entity.Custodian
import com.acuo.persist.entity.CustodianAccount
import com.acuo.persist.services.CustodianAccountService

import javax.inject.Inject

class CustodianAccountFactory extends AbstractFactory implements BuilderFactory {

    private final CustodianAccountService service

    @Inject
    CustodianAccountFactory(CustodianAccountService service) {
        this.service = service
    }

    @Override
    String name() {
        return "account"
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
        def entity = child as CustodianAccount
        if (parent != null) {
            switch (parent) {
                case Custodian:
                    parent.custodianAccounts << entity
                    break
            }
        }
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder,
                         Object parent, Object child) {
        service.save(child)
    }

    private CustodianAccount getOrCreate(Map attributes) {
        CustodianAccount entity
        if (attributes != null) {
            String id = attributes["accountId"]
            if (id != null) {
                entity = service.find(id)
            }
            if (entity == null) {
                return new CustodianAccount(attributes)
            } else
                return entity
        } else {
            entity = new CustodianAccount()
        }
        return entity
    }
}
