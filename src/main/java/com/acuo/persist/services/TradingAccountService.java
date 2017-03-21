package com.acuo.persist.services;

import com.acuo.persist.entity.Trade;
import com.acuo.persist.entity.TradingAccount;

public interface TradingAccountService extends Service<TradingAccount> {

    void addTrade(String accountId, Trade trade);
}
