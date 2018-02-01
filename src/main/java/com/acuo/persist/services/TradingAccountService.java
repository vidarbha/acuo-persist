package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.persist.entity.trades.Trade;
import com.acuo.persist.entity.TradingAccount;

public interface TradingAccountService extends Service<TradingAccount, Long> {

    void addTrade(ClientId clientId, String accountId, Trade trade);

    Iterable<TradingAccount> accounts(ClientId clientId, String... accountIds);

    TradingAccount account(ClientId clientId, String accountId);
}
