package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioName;
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
    public Iterable<Portfolio> portfolios(ClientId clientId, PortfolioName... portfolioNames) {
        String query =
        "MATCH (client:Client)-[:MANAGES]-(legal:LegalEntity)-[]-(a:Agreement)<-[:FOLLOWS]-(portfolio:Portfolio) " +
        "WHERE client.id = {clientId} " +
        "AND portfolio.name IN {portfolioNames} " +
        "WITH portfolio " +
        "MATCH p=()-[*0..1]-(portfolio)-[:FOLLOWS]->(:Agreement)-[]-(:LegalEntity)-[:MANAGES]-(:Firm) " +
        "RETURN p";
        return dao.getSession().query(Portfolio.class, query, ImmutableMap.of(
                "clientId", clientId.toString(),
                "portfolioNames", portfolioNames));
    }

    @Override
    @Transactional
    public Portfolio portfolio(ClientId clientId, PortfolioName portfolioName) {
        final Iterable<Portfolio> portfolios = portfolios(clientId, portfolioName);
        if (Iterables.count(portfolios) == 0) {
            log.warn("portfolio with portfolioId {} and client {} doesn't exist", portfolioName, clientId);
            return null;
        }
        return Iterables.single(portfolios);
    }

    @Override
    @Transactional
    public Long tradeCount(ClientId clientId, PortfolioName portfolioName) {
        String query =
            "MATCH (client:Client)-[:MANAGES]-(:LegalEntity)-[]-(:Agreement)<-[:FOLLOWS]-" +
            "(portfolio:Portfolio)<-[:BELONGS_TO]-(trade:Trade) " +
            "WHERE client.id = {clientId} " +
            "AND portfolio.name = {portfolioName} " +
            "RETURN count(trade) as count";
        final ImmutableMap<String, String> parameters = ImmutableMap.of(
                "clientId", clientId.toString(),
                "portfolioName", portfolioName.toString());
        Result result =  dao.getSession().query(query, parameters);
        Map<String, Object> next = result.iterator().next();
        return (Long) next.get("count");
    }
}
