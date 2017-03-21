package com.acuo.persist.services;

import com.acuo.persist.entity.Trade;
import com.acuo.persist.entity.TradingAccount;
import com.google.inject.persist.Transactional;


public class TradingAccountServiceImpl extends GenericService<TradingAccount> implements TradingAccountService {

    @Override
    public Class<TradingAccount> getEntityType() {
        return TradingAccount.class;
    }

    public void addTrade(String accountId, Trade trade) {
        TradingAccount account = findById(accountId, 2);
        account.remove(trade);
        account.add(trade);
        save(account);
    }
}
