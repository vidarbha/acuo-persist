package com.acuo.persist.services;

import com.acuo.persist.entity.Portfolio;
import com.acuo.persist.ids.PortfolioId;
import com.acuo.persist.ids.TradeId;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
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
    public Long tradeCount(PortfolioId portfolioId) {
        String query =
                "MATCH p=(portfolio:Portfolio {id:{id}})<-[:BELONGS_TO]-(trade:Trade) " +
                        "RETURN count(trade) as count";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", portfolioId.toString());
        Result result =  sessionProvider.get().query(query, parameters);
        Map<String, Object> next = result.iterator().next();
        Long count = (Long) next.get("count");
        return count;
    }
}
