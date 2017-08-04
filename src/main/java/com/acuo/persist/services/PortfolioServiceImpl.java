package com.acuo.persist.services;

import com.acuo.common.model.ids.PortfolioId;
import com.acuo.common.model.ids.TradeId;
import com.acuo.persist.entity.Portfolio;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.driver.internal.util.Iterables;
import org.neo4j.ogm.model.Result;

import java.util.Map;

public class PortfolioServiceImpl extends GenericService<Portfolio, PortfolioId> implements PortfolioService {

    @Override
    public Class<Portfolio> getEntityType() {
        return Portfolio.class;
    }

    @Override
    @Transactional
    public Portfolio findBy(TradeId tradeId) {
        String query =
                "MATCH p=(portfolio:Portfolio)<-[:BELONGS_TO]-(trade:Trade {id:{id}}) " +
                "RETURN p, nodes(p), relationships(p)";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", tradeId.toString());
        return sessionProvider.get().queryForObject(Portfolio.class, query, parameters);
    }

    @Override
    @Transactional
    public Iterable<Portfolio> portfolios(PortfolioId... ids) {
        String query =
                "MATCH p=(firm:Firm)-[:MANAGES]-(legal:LegalEntity)-[:CLIENT_SIGNS|COUNTERPARTY_SIGNS]-" +
                "(a:Agreement)<-[:FOLLOWS]-(portfolio:Portfolio) " +
                "WHERE portfolio.id IN {ids} " +
                "RETURN portfolio, nodes(p), relationships(p)";
        return sessionProvider.get().query(Portfolio.class, query, ImmutableMap.of("ids", ids));
    }

    @Override
    @Transactional
    public Portfolio portfolio(PortfolioId id) {
        return Iterables.single(portfolios(id));
    }

    @Override
    @Transactional
    public Long tradeCount(PortfolioId portfolioId) {
        String query =
                "MATCH (portfolio:Portfolio {id:{id}})<-[:BELONGS_TO]-(trade:Trade) " +
                "RETURN count(trade) as count";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", portfolioId.toString());
        Result result =  sessionProvider.get().query(query, parameters);
        Map<String, Object> next = result.iterator().next();
        return (Long) next.get("count");
    }
}
