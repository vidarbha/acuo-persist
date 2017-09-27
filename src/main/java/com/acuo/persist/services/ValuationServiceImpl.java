package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.common.model.ids.PortfolioId;
import com.acuo.common.model.ids.TradeId;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.AssetValuation;
import com.acuo.persist.entity.MarginValuation;
import com.acuo.persist.entity.MarginValue;
import com.acuo.persist.entity.Portfolio;
import com.acuo.persist.entity.Trade;
import com.acuo.persist.entity.TradeValuation;
import com.acuo.persist.entity.TradeValue;
import com.acuo.persist.entity.Valuation;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import java.time.LocalDate;
import java.util.stream.StreamSupport;

import static com.acuo.common.util.ArgChecker.notNull;

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
                "MATCH p=(value:TradeValue)<-[:VALUE*0..1]-(valuation:TradeValuation)-[:VALUATED]->(trade:Trade {id:{id}})-[*0..1]-(n) " +
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
            Trade trade = tradeService.find(tradeId);
            valuation.setTrade(notNull(trade, "trade"));
            valuation = createOrUpdate(valuation);
        }
        return valuation;
    }

    @Override
    @Transactional
    public MarginValuation getMarginValuationFor(PortfolioId portfolioId, Types.CallType callType) {
        String query =
                "MATCH p=(value)<-[:VALUE*0..1]-(valuation:MarginValuation {callType: {callType}})" +
                        "-[:VALUATED]->(portfolio:Portfolio {id:{id}})-[:BELONGS_TO|FOLLOWS|PART_OF]-(n) " +
                        "RETURN p, nodes(p), relationships(p)";
        final String pId = portfolioId.toString();
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", pId, "callType", callType.name());
        return sessionProvider.get().queryForObject(MarginValuation.class, query, parameters);
    }

    @Override
    @Transactional
    public MarginValuation getOrCreateMarginValuationFor(PortfolioId portfolioId, Types.CallType callType) {
        MarginValuation valuation = getMarginValuationFor(portfolioId, callType);
        if (valuation == null) {
            valuation = new MarginValuation();
            Portfolio portfolio = portfolioService.find(portfolioId);
            valuation.setPortfolio(notNull(portfolio, "portfolio"));
            valuation.setCallType(callType);
            valuation = createOrUpdate(valuation);
        }
        return valuation;
    }

    @Override
    @Transactional
    public AssetValuation getAssetValuationFor(AssetId assetId) {
        String query =
                "MATCH p=(value:AssetValue)<-[:VALUE*0..1]-(valuation:AssetValuation)-[:VALUATED]->(asset:Asset {id:{id}})-[*0..1]-(n) " +
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
            Asset asset = assetService.find(assetId);
            valuation.setAsset(notNull(asset, "asset"));
            valuation = save(valuation, 1);
        }
        return valuation;
    }

    @Override
    @Transactional
    public Long tradeCount(PortfolioId portfolioId) {
        return portfolioService.tradeCount(portfolioId);
    }

    @Override
    @Transactional
    public Long tradeValuedCount(PortfolioId portfolioId, LocalDate valuationDate) {
        String query =
                "MATCH p=(portfolio:Portfolio {id:{id}})<-[:BELONGS_TO]-(trade:Trade)<-[:VALUATED]-(valuation:TradeValuation)-[:VALUE]->(value:TradeValue) " +
                "RETURN valuation, nodes(p), relationships(p)";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", portfolioId.toString());
        Iterable<TradeValuation> valuations = sessionProvider.get().query(TradeValuation.class, query, parameters);
        return StreamSupport.stream(valuations.spliterator(), true)
                .filter(valuation -> valuation.isValuedFor(valuationDate))
                .count();
    }

    @Override
    @Transactional
    public boolean isTradeValuated(TradeId tradeId, LocalDate valuationDate) {
        String query =
                "MATCH (value:TradeValue {valuationDate:{date}})<-[:VALUE]-(valuation:TradeValuation)-[:VALUATED]->(trade:Trade {id:{id}})" +
                "RETURN value";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", tradeId.toString(),
                "date", new LocalDateConverter().toGraphProperty(valuationDate));
        Iterable<TradeValue> values = sessionProvider.get().query(TradeValue.class, query, parameters);
        return values != null && !Iterables.isEmpty(values);
    }

    @Override
    @Transactional
    public boolean isPortfolioValuated(PortfolioId portfolioId, Types.CallType callType, LocalDate valuationDate) {
        String query =
                "MATCH (value:MarginValue {valuationDate:{date}})<-[:VALUE]-(valuation:MarginValuation {callType:{callType}})-[:VALUATED]->(portfolio:Portfolio {id:{id}})" +
                "RETURN value";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", portfolioId.toString(),
                "callType", callType.name(),
                "date", new LocalDateConverter().toGraphProperty(valuationDate));
        Iterable<MarginValue> values = sessionProvider.get().query(MarginValue.class, query, parameters);
        return values != null && !Iterables.isEmpty(values);
    }
}