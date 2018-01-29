package com.acuo.persist.services;

import com.acuo.persist.entity.trades.Trade;
import com.acuo.persist.entity.TradingAccount;

public interface TradingAccountService extends Service<TradingAccount, String> {

    void addTrade(String accountId, Trade trade);
}
