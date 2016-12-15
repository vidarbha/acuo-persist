package com.acuo.persist.services;

import com.acuo.persist.entity.Account;
import com.google.inject.persist.Transactional;

@Transactional
public class AccountServiceImpl extends GenericService<Account> implements AccountService {
    @Override
    public Class<Account> getEntityType() {
        return Account.class;
    }
}
