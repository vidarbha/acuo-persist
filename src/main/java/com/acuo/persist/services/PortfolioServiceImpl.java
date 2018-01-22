package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioId;
import com.acuo.common.ids.TradeId;
import com.acuo.persist.entity.Portfolio;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.driver.internal.util.Iterables;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;

public class PortfolioServiceImpl extends AbstractService<Portfolio, PortfolioId> implements PortfolioService {

    @Inject
    public PortfolioServiceImpl(Provider<Session> session) {
        super(session);
    }

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
        return dao.getSession().queryForObject(Portfolio.class, query, parameters);
    }

    @Override
    @Transactional
    public Iterable<Portfolio> portfolios(ClientId clientId, PortfolioId... ids) {
        String query =
        "MATCH p=(firm:Firm {id:{clientId}})-[:MANAGES]-(legal:LegalEntity)-[]-(a:Agreement)<-[:FOLLOWS]-(portfolio:Portfolio) " +
        "WHERE portfolio.id IN {ids} " +
        "RETURN p";
        return dao.getSession().query(Portfolio.class, query, ImmutableMap.of("clientId", clientId.toString(), "ids", ids));
    }

    @Override
    @Transactional
    public Portfolio portfolio(ClientId clientId, PortfolioId id) {
        return Iterables.single(portfolios(clientId, id));
    }

    @Override
    @Transactional
    public Long tradeCount(ClientId clientId, PortfolioId portfolioId) {
        String query =
            "MATCH (firm:Firm {id:{clientId}})-[:MANAGES]-(legal:LegalEntity)-[]-(a:Agreement)<-[:FOLLOWS]-" +
            "(portfolio:Portfolio {id:{id}})<-[:BELONGS_TO]-(trade:Trade) " +
            "RETURN count(trade) as count";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("clientId", clientId.toString(),
                "id", portfolioId.toString());
        Result result =  dao.getSession().query(query, parameters);
        Map<String, Object> next = result.iterator().next();
        return (Long) next.get("count");
    }
}
