package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioId;
import com.acuo.common.ids.TradeId;
import com.acuo.common.model.margin.Types;
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
import org.neo4j.ogm.session.Session;

import javax.inject.Provider;
import java.time.LocalDate;

import static com.acuo.common.util.ArgChecker.notNull;

public class ValuationServiceImpl extends AbstractService<Valuation, String> implements ValuationService {

    private final PortfolioService portfolioService;
    private final AssetService assetService;
    private final TradeService<Trade> tradeService;

    @Inject
    public ValuationServiceImpl(Provider<Session> session,
                                PortfolioService portfolioService,
                                AssetService assetService,
                                TradeService<Trade> tradeService) {
        super(session);
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
        return dao.getSession().queryForObject(TradeValuation.class, query, parameters);
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
        return dao.getSession().queryForObject(MarginValuation.class, query, parameters);
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
    public Long tradeCount(ClientId clientId, PortfolioId portfolioId) {
        return portfolioService.tradeCount(clientId, portfolioId);
    }

    @Override
    @Transactional
    public Long tradeValuedCount(PortfolioId portfolioId, LocalDate valuationDate) {
        String query =
                "MATCH (portfolio:Portfolio {id:{id}})<-[:BELONGS_TO]-(trade:Trade)" +
                "<-[:VALUATED]-(valuation:TradeValuation)-[:VALUE]->(value:TradeValue) " +
                "WHERE value.valuationDate = {valDateStr} " +
                "WITH DISTINCT trade " +
                "RETURN count(trade)";
        final String valDateStr = new LocalDateConverter().toGraphProperty(valuationDate);
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", portfolioId.toString(),
                "valDateStr", valDateStr);
        return dao.getSession().queryForObject(Long.class, query, parameters);
    }

    @Override
    @Transactional
    public Long tradeNotValuedCount(PortfolioId portfolioId, LocalDate valuationDate) {
        String query =
                "MATCH (portfolio:Portfolio {id:{id}})<-[:BELONGS_TO]-(trade:Trade)-[:ENCOUNTERS]-(error:ServiceError) " +
                "WHERE error.valuationDate = {valDateStr}" +
                "WITH DISTINCT trade " +
                "RETURN count(trade)";
        final String valDateStr = new LocalDateConverter().toGraphProperty(valuationDate);
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", portfolioId.toString(), "valDateStr", valDateStr);
        return dao.getSession().queryForObject(Long.class, query, parameters);
    }

    @Override
    @Transactional
    public boolean isTradeValuated(TradeId tradeId, LocalDate valuationDate) {
        String query =
                "MATCH (value:TradeValue {valuationDate:{date}})<-[:VALUE]-(valuation:TradeValuation)-[:VALUATED]->(trade:Trade {id:{id}})" +
                "RETURN value";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", tradeId.toString(),
                "date", new LocalDateConverter().toGraphProperty(valuationDate));
        Iterable<TradeValue> values = dao.getSession().query(TradeValue.class, query, parameters);
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
        Iterable<MarginValue> values = dao.getSession().query(MarginValue.class, query, parameters);
        return values != null && !Iterables.isEmpty(values);
    }
}