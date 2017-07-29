package com.acuo.persist.services;

import com.acuo.persist.entity.CustodianAccount;
import com.acuo.persist.entity.LegalEntity;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

public class CustodianAccountServiceImpl extends GenericService<CustodianAccount, String> implements CustodianAccountService {

    @Override
    public Class<CustodianAccount> getEntityType() {
        return CustodianAccount.class;
    }

    @Override
    @Transactional
    public Iterable<CustodianAccount> custodianAccountsFor(LegalEntity legalEntity) {
        String query =
                "MATCH (entity:LegalEntity {id:{id}})-[:HAS]->(trading:TradingAccount)-[r:ACCESSES]->(account:CustodianAccount) " +
                "RETURN account";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", legalEntity.getLegalEntityId());
        return  sessionProvider.get().query(CustodianAccount.class, query, parameters);
    }

    @Override
    @Transactional
    public Iterable<CustodianAccount> counterPartyCustodianAccountsFor(LegalEntity legalEntity) {
        String query = "MATCH (entity:LegalEntity {id:{id}})-[r:ACCESSES]->(account:CustodianAccount) RETURN account";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", legalEntity.getLegalEntityId());
        return  sessionProvider.get().query(CustodianAccount.class, query, parameters);
    }

}
