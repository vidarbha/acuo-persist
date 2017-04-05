package com.acuo.persist.services;

import com.acuo.persist.entity.MarginValuation;
import com.acuo.persist.entity.Portfolio;
import com.acuo.persist.entity.TradeValuation;
import com.acuo.persist.entity.Valuation;
import com.acuo.persist.ids.PortfolioId;
import com.acuo.persist.ids.TradeId;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class ValuationServiceImpl extends GenericService<Valuation> implements ValuationService {

    @Inject
    PortfolioService portfolioService;

    @Override
    public Class<Valuation> getEntityType() {
        return Valuation.class;
    }

    @Override
    @Transactional
    public TradeValuation getOrCreateTradeValuationFor(TradeId tradeId) {
        final Portfolio portfolio = portfolioService.findBy(tradeId);
        final PortfolioId portfolioId = PortfolioId.fromString(portfolio.getPortfolioId());
        return getOrCreateTradeValuationFor(portfolioId);
    }

    @Override
    @Transactional
    public TradeValuation getOrCreateTradeValuationFor(PortfolioId portfolioId) {
        String query =
                "MATCH p=(n:TradeValuation)<-[:VALUATED]-(:Portfolio {id:{id}}) " +
                "RETURN p, nodes(p), rels(p)";
        final String pId = portfolioId.toString();
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", pId);
        TradeValuation valuation = sessionProvider.get().queryForObject(TradeValuation.class, query, parameters);
        if (valuation == null) {
            valuation = new TradeValuation();
            valuation.setPortfolio(portfolioService.findById(pId));
            valuation = createOrUpdate(valuation);
        }
        return valuation;
    }

    @Override
    @Transactional
    public MarginValuation getOrCreateMarginValuationFor(PortfolioId portfolioId) {
        String query =
                "MATCH p=(n:MarginValuation)<-[:VALUATED]-(:Portfolio {id:{id}}) " +
                "RETURN p, nodes(p), rels(p)";
        final String pId = portfolioId.toString();
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", pId);
        MarginValuation valuation = sessionProvider.get().queryForObject(MarginValuation.class, query, parameters);
        if (valuation == null) {
            valuation = new MarginValuation();
            valuation.setPortfolio(portfolioService.findById(pId));
            valuation = createOrUpdate(valuation);
        }
        return valuation;
    }
}
