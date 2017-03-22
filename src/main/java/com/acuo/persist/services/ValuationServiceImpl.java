package com.acuo.persist.services;

import com.acuo.persist.entity.Valuation;
import com.acuo.persist.ids.PortfolioId;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

public class ValuationServiceImpl extends GenericService<Valuation> implements ValuationService {

    @Override
    public Class<Valuation> getEntityType() {
        return Valuation.class;
    }

    @Override
    @Transactional
    public Iterable<Valuation> allTradeValuationFor(PortfolioId portfolioId) {
        String query =
                "MATCH p=(n:TradeValuation)<-[:VALUATED]-(:Trade)-[:BELONGS_TO]->(:Portfolio {id:{id}}) " +
                "RETURN p, nodes(p), rels(p)";
        return sessionProvider.get().query(getEntityType(), query, ImmutableMap.of("id",portfolioId.toString()));
    }

    @Override
    @Transactional
    public Iterable<Valuation> allMarginValuationFor(PortfolioId portfolioId) {
        String query =
                "MATCH p=(n:MarginValuation)<-[:VALUATED]-(:Portfolio {id:{id}}) " +
                "RETURN p, nodes(p), rels(p)";
        return sessionProvider.get().query(getEntityType(), query, ImmutableMap.of("id",portfolioId.toString()));
    }
}
