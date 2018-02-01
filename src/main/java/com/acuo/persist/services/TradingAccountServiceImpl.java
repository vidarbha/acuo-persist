package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.persist.entity.TradingAccount;
import com.acuo.persist.entity.trades.Trade;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.internal.util.Iterables;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

@Slf4j
public class TradingAccountServiceImpl extends AbstractService<TradingAccount, Long> implements TradingAccountService {

    @Inject
    public TradingAccountServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<TradingAccount> getEntityType() {
        return TradingAccount.class;
    }

    @Override
    @Transactional
    public Iterable<TradingAccount> accounts(ClientId clientId, String... accountIds) {
        String query =
                "MATCH (client:Client)-[:MANAGES]-(legal:LegalEntity)-[:HAS]->(account:TradingAccount) " +
                "WHERE client.id = {clientId} " +
                "AND account.id IN {accountIds} " +
                "WITH account " +
                "MATCH p=()-[*0..1]-(account) " +
                "RETURN p";
        return dao.getSession().query(TradingAccount.class, query, ImmutableMap.of("clientId", clientId.toString(), "accountIds", accountIds));
    }

    @Override
    @Transactional
    public TradingAccount account(ClientId clientId, String accountId) {
        final Iterable<TradingAccount> accounts = accounts(clientId, accountId);
        if (Iterables.count(accounts) == 0) {
            log.warn("trading account with id {} and client {} doesn't exist", accountId, clientId);
            return null;
        }
        return Iterables.single(accounts);
    }

    @Override
    @Transactional
    public void addTrade(ClientId clientId, String accountId, Trade trade) {
        TradingAccount account = account(clientId, accountId);
        if (account == null) {
            log.warn("can't add trade {} to account with id {} and client {} doesn't exist", trade.getTradeId(), accountId, clientId);
            return;
        }
        account.remove(trade);
        account.add(trade);
        save(account);
    }
}
