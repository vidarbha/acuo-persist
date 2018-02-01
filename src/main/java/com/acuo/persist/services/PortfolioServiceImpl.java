package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioId;
import com.acuo.common.ids.TradeId;
import com.acuo.persist.entity.Portfolio;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.internal.util.Iterables;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;

@Slf4j
public class PortfolioServiceImpl extends AbstractService<Portfolio, Long> implements PortfolioService {

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
    public Iterable<Portfolio> portfolios(ClientId clientId, PortfolioId... portfolioIds) {
        String query =
        "MATCH (client:Client)-[:MANAGES]-(legal:LegalEntity)-[]-(a:Agreement)<-[:FOLLOWS]-(portfolio:Portfolio) " +
        "WHERE client.id = {clientId} " +
        "AND portfolio.id IN {portfolioIds} " +
        "WITH portfolio " +
        "MATCH p=()-[*0..1]-(portfolio)-[:FOLLOWS]->(:Agreement)-[]-(:LegalEntity)-[:MANAGES]-(:Firm) " +
        "RETURN p";
        return dao.getSession().query(Portfolio.class, query, ImmutableMap.of(
                "clientId", clientId.toString(),
                "portfolioIds", portfolioIds));
    }

    @Override
    @Transactional
    public Portfolio portfolio(ClientId clientId, PortfolioId portfolioId) {
        final Iterable<Portfolio> portfolios = portfolios(clientId, portfolioId);
        if (Iterables.count(portfolios) == 0) {
            log.warn("portfolio with portfolioId {} and client {} doesn't exist", portfolioId, clientId);
            return null;
        }
        return Iterables.single(portfolios);
    }

    @Override
    @Transactional
    public Long tradeCount(ClientId clientId, PortfolioId portfolioId) {
        String query =
            "MATCH (client:Client)-[:MANAGES]-(:LegalEntity)-[]-(:Agreement)<-[:FOLLOWS]-" +
            "(portfolio:Portfolio)<-[:BELONGS_TO]-(trade:Trade) " +
            "WHERE client.id = {clientId} " +
            "AND portfolio.id = {portfolioId} " +
            "RETURN count(trade) as count";
        final ImmutableMap<String, String> parameters = ImmutableMap.of(
                "clientId", clientId.toString(),
                "portfolioId", portfolioId.toString());
        Result result =  dao.getSession().query(query, parameters);
        Map<String, Object> next = result.iterator().next();
        return (Long) next.get("count");
    }
}
