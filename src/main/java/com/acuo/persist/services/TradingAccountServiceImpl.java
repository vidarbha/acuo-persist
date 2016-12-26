package com.acuo.persist.services;

import com.acuo.persist.entity.TradingAccount;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

@Transactional
public class TradingAccountServiceImpl extends GenericService<TradingAccount> implements TradingAccountService {
    @Override
    public Class<TradingAccount> getEntityType() {
        return TradingAccount.class;
    }

    public TradingAccount findById(String id) {
        String query =  "MATCH (account:TradingAccount {accountId: {id} }) " +
                        "WITH account " +
                        "MATCH p=(account)-[r*0..1]-() RETURN account, nodes(p), rels(p)";
        return session.queryForObject(getEntityType(), query, ImmutableMap.of("id",id));
    }
}
