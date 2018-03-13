package com.acuo.persist.services;

import com.acuo.persist.entity.CustodianAccount;
import com.acuo.persist.entity.LegalEntity;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class CustodianAccountServiceImpl extends AbstractService<CustodianAccount, String> implements CustodianAccountService {

    @Inject
    public CustodianAccountServiceImpl(Provider<Session> session) {
        super(session);
    }

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
        return  dao.getSession().query(CustodianAccount.class, query, parameters);
    }

    @Override
    @Transactional
    public Iterable<CustodianAccount> counterPartyCustodianAccountsFor(LegalEntity legalEntity) {
        String query = "MATCH (entity:LegalEntity {id:{id}})-[r:ACCESSES]->(account:CustodianAccount) RETURN account";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", legalEntity.getLegalEntityId());
        return  dao.getSession().query(CustodianAccount.class, query, parameters);
    }

}
