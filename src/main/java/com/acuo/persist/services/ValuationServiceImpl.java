package com.acuo.persist.services;

import com.acuo.persist.entity.AssetValuation;
import com.acuo.persist.entity.MarginValuation;
import com.acuo.persist.entity.Trade;
import com.acuo.persist.entity.TradeValuation;
import com.acuo.persist.entity.Valuation;
import com.acuo.persist.ids.AssetId;
import com.acuo.persist.ids.PortfolioId;
import com.acuo.persist.ids.TradeId;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class ValuationServiceImpl extends GenericService<Valuation, String> implements ValuationService {

    private final PortfolioService portfolioService;
    private final AssetService assetService;
    private final TradeService<Trade> tradeService;

    @Inject
    public ValuationServiceImpl(PortfolioService portfolioService,
                                AssetService assetService,
                                TradeService<Trade> tradeService) {
        this.portfolioService = portfolioService;
        this.assetService = assetService;
        this.tradeService = tradeService;
    }

    @Override
    public Class<Valuation> getEntityType() {
        return Valuation.class;
    }


    @Override
    @Transactional
    public TradeValuation getTradeValuationFor(TradeId tradeId) {
        String query =
                "MATCH p=(value:TradeValue)<-[:VALUE]-(valuation:TradeValuation)-[:VALUATED]->(trade:Trade {id:{id}})-[*0..1]-(n) " +
                        "RETURN p, nodes(p), relationships(p)";
        final String id = tradeId.toString();
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", id);
        return sessionProvider.get().queryForObject(TradeValuation.class, query, parameters);
    }

    @Override
    @Transactional
    public TradeValuation getOrCreateTradeValuationFor(TradeId tradeId) {
        TradeValuation valuation = getTradeValuationFor(tradeId);
        if (valuation == null) {
            valuation = new TradeValuation();
            valuation.setTrade(tradeService.find(tradeId));
            valuation = createOrUpdate(valuation);
        }
        return valuation;
    }

    @Override
    @Transactional
    public MarginValuation getMarginValuationFor(PortfolioId portfolioId) {
        String query =
                "MATCH p=(value:MarginValue)<-[:VALUE]-(valuation:MarginValuation)-[:VALUATED]->(portfolio:Portfolio {id:{id}})-[*0..1]-(n) " +
                        "RETURN p, nodes(p), relationships(p)";
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
            valuation.setPortfolio(portfolioService.find(portfolioId));
            valuation = createOrUpdate(valuation);
        }
        return valuation;
    }

    @Override
    @Transactional
    public AssetValuation getAssetValuationFor(AssetId assetId) {
        String query =
                "MATCH p=(value:AssetValue)<-[:VALUE]-(valuation:AssetValuation)-[:VALUATED]->(asset:Asset {id:{id}})-[*0..1]-(n) " +
                        "RETURN p, nodes(p), relationships(p)";
        final String aId = assetId.toString();
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", aId);
        return sessionProvider.get().queryForObject(AssetValuation.class, query, parameters);
    }

    @Override
    @Transactional
    public AssetValuation getOrCreateAssetValuationFor(AssetId assetId) {
        AssetValuation valuation = getAssetValuationFor(assetId);
        if (valuation == null) {
            valuation = new AssetValuation();
            valuation.setAsset(assetService.find(assetId.toString()));
            valuation = save(valuation, 1);
        }
        return valuation;
    }
}