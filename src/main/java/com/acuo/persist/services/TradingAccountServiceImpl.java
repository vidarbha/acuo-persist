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
}
