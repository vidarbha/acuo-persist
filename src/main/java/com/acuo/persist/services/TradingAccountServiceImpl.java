package com.acuo.persist.services;

import com.acuo.persist.entity.trades.Trade;
import com.acuo.persist.entity.TradingAccount;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;


public class TradingAccountServiceImpl extends AbstractService<TradingAccount, String> implements TradingAccountService {

    @Inject
    public TradingAccountServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<TradingAccount> getEntityType() {
        return TradingAccount.class;
    }

    @Transactional
    public void addTrade(String accountId, Trade trade) {
        TradingAccount account = find(accountId, 2);
        account.remove(trade);
        account.add(trade);
        save(account);
    }
}
