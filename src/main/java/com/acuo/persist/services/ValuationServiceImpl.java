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
    public TradeValuation getTradeValuationFor(TradeId tradeId) {
        final Portfolio portfolio = portfolioService.findBy(tradeId);
        return getTradeValuationFor(portfolio.getPortfolioId());
    }

    @Override
    @Transactional
    public TradeValuation getOrCreateTradeValuationFor(TradeId tradeId) {
        final Portfolio portfolio = portfolioService.findBy(tradeId);
        return getOrCreateTradeValuationFor(portfolio.getPortfolioId());
    }

    @Override
    @Transactional
    public TradeValuation getTradeValuationFor(PortfolioId portfolioId) {
        String query =
                "MATCH p=(value:TradeValue)<-[:VALUE]-(n:TradeValuation)<-[:VALUATED]-(:Portfolio {id:{id}}) " +
                        "RETURN p, nodes(p), rels(p)";
        final String pId = portfolioId.toString();
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", pId);
        return sessionProvider.get().queryForObject(TradeValuation.class, query, parameters);
    }

    @Override
    @Transactional
    public TradeValuation getOrCreateTradeValuationFor(PortfolioId portfolioId) {
        TradeValuation valuation = getTradeValuationFor(portfolioId);
        if (valuation == null) {
            valuation = new TradeValuation();
            valuation.setPortfolio(portfolioService.findById(portfolioId.toString()));
            valuation = createOrUpdate(valuation);
        }
        return valuation;
    }

    @Override
    @Transactional
    public MarginValuation getMarginValuationFor(PortfolioId portfolioId) {
        String query =
                "MATCH p=(value:MarginValue)<-[:VALUE]-(n:MarginValuation)<-[:VALUATED]-(:Portfolio {id:{id}}) " +
                        "RETURN p, nodes(p), rels(p)";
        final String pId = portfolioId.toString();
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", pId);
       return sessionProvider.get().queryForObject(MarginValuation.class, query, parameters);
    }

    @Override
    @Transactional
    public MarginValuation getOrCreateMarginValuationFor(PortfolioId portfolioId) {
        MarginValuation valuation = getMarginValuationFor(portfolioId);
        if (valuation == null) {
            valuation = new MarginValuation();
            valuation.setPortfolio(portfolioService.findById(portfolioId.toString()));
            valuation = createOrUpdate(valuation);
        }
        return valuation;
    }
}
