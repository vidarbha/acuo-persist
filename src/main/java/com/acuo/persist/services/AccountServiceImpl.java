package com.acuo.persist.services;

import com.acuo.persist.entity.Account;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

@Transactional
public class AccountServiceImpl extends GenericService<Account> implements AccountService {
    @Override
    public Class<Account> getEntityType() {
        return Account.class;
    }

    public Account findById(String id) {
        String query =  "MATCH (account:Account {accountId: {id} }) " +
                        "WITH account " +
                        "MATCH p=(account)-[r*0..1]-() RETURN account, nodes(p), rels(p)";
        return session.queryForObject(getEntityType(), query, ImmutableMap.of("id",id));
    }
}
