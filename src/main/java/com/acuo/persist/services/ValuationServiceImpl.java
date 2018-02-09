package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioName;
import com.acuo.common.ids.TradeId;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.MarginValuation;
import com.acuo.persist.entity.MarginValue;
import com.acuo.persist.entity.Portfolio;
import com.acuo.persist.entity.TradeValuation;
import com.acuo.persist.entity.TradeValue;
import com.acuo.persist.entity.Valuation;
import com.acuo.persist.entity.trades.Trade;
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
    private final TradeService<Trade> tradeService;

    @Inject
    public ValuationServiceImpl(Provider<Session> session,
                                PortfolioService portfolioService,
                                TradeService<Trade> tradeService) {
        super(session);
        this.portfolioService = portfolioService;
        this.tradeService = tradeService;
    }

    @Override
    public Class<Valuation> getEntityType() {
        return Valuation.class;
    }


    @Override
    @Transactional
    public TradeValuation getTradeValuationFor(ClientId clientId, TradeId tradeId) {
        String query =  "MATCH (client:Client) " +
                        "-[:MANAGES]-> (:LegalEntity) " +
                        "-[:HAS]-> (:TradingAccount) " +
                        "-[:POSITIONS_ON]-> (trade:Trade) " +
                        "WHERE client.id = {clientId} " +
                        "AND trade.id = {tradeId}" +
                        "WITH trade " +
                        "MATCH p=(value:TradeValue)<-[:VALUE*0..1]-(valuation:TradeValuation)-[:VALUATED]->(trade)-[*0..1]-(n) " +
                        "RETURN p";
        final ImmutableMap<String, String> parameters = ImmutableMap.of(
                "clientId", clientId.toString(),
                "tradeId", tradeId.toString());
        return dao.getSession().queryForObject(TradeValuation.class, query, parameters);
    }

    @Override
    @Transactional
    public TradeValuation getOrCreateTradeValuationFor(ClientId clientId, TradeId tradeId) {
        TradeValuation valuation = getTradeValuationFor(clientId, tradeId);
        if (valuation == null) {
            valuation = new TradeValuation();
            Trade trade = tradeService.findTradeBy(clientId, tradeId);
            valuation.setTrade(notNull(trade, "trade"));
            valuation = createOrUpdate(valuation);
        }
        return valuation;
    }

    @Override
    @Transactional
    public MarginValuation getMarginValuationFor(ClientId clientId, PortfolioName portfolioName, Types.CallType callType) {
        String query =
            "MATCH (client:Client)-[:MANAGES]-(legal:LegalEntity)-[]-(a:Agreement)<-[:FOLLOWS]-(portfolio:Portfolio) " +
            "WHERE client.id = {clientId} " +
            "AND portfolio.name = {portfolioName}" +
            "WITH portfolio " +
            "MATCH p=(value)<-[:VALUE*0..1]-(valuation:MarginValuation {callType: {callType}})" +
            "-[:VALUATED]->(portfolio)-[:BELONGS_TO|FOLLOWS|PART_OF]-(n) " +
            "RETURN p";
        final ImmutableMap<String, String> parameters = ImmutableMap.of(
                "clientId", clientId.toString(),
                "portfolioName", portfolioName.toString(),
                "callType", callType.name());
        return dao.getSession().queryForObject(MarginValuation.class, query, parameters);
    }

    @Override
    @Transactional
    public MarginValuation getOrCreateMarginValuationFor(ClientId clientId, PortfolioName portfolioName, Types.CallType callType) {
        MarginValuation valuation = getMarginValuationFor(clientId, portfolioName, callType);
        if (valuation == null) {
            valuation = new MarginValuation();
            Portfolio portfolio = portfolioService.portfolio(clientId, portfolioName);
            valuation.setPortfolio(notNull(portfolio, "portfolio"));
            valuation.setCallType(callType);
            valuation = createOrUpdate(valuation);
        }
        return valuation;
    }

    @Override
    @Transactional
    public Long tradeCount(ClientId clientId, PortfolioName portfolioName) {
        return portfolioService.tradeCount(clientId, portfolioName);
    }

    @Override
    @Transactional
    public Long tradeValuedCount(ClientId clientId, PortfolioName portfolioName, LocalDate valuationDate) {
        String query =
                "MATCH (client:Client)-[:MANAGES]-(legal:LegalEntity)-[]-(a:Agreement)<-[:FOLLOWS]-" +
                "(portfolio:Portfolio)<-[:BELONGS_TO]-(trade:Trade)" +
                "<-[:VALUATED]-(valuation:TradeValuation)-[:VALUE]->(value:TradeValue) " +
                "WHERE client.id = {clientId} " +
                "AND portfolio.name = {portfolioName}" +
                "AND value.valuationDate = {valDateStr} " +
                "WITH DISTINCT trade " +
                "RETURN count(trade)";
        final String valDateStr = new LocalDateConverter().toGraphProperty(valuationDate);
        final ImmutableMap<String, String> parameters = ImmutableMap.of(
                "clientId", clientId.toString(),
                "portfolioName", portfolioName.toString(),
                "valDateStr", valDateStr);
        return dao.getSession().queryForObject(Long.class, query, parameters);
    }

    @Override
    @Transactional
    public Long tradeNotValuedCount(ClientId clientId, PortfolioName portfolioName, LocalDate valuationDate) {
        String query =
                "MATCH (client:Client)-[:MANAGES]-(legal:LegalEntity)-[]-(a:Agreement)<-[:FOLLOWS]-" +
                "(portfolio:Portfolio)<-[:BELONGS_TO]-(trade:Trade)-[:ENCOUNTERS]-(error:ServiceError) " +
                "WHERE client.id = {clientId} " +
                "AND portfolio.name = {portfolioName}" +
                "AND error.valuationDate = {valDateStr}" +
                "WITH DISTINCT trade " +
                "RETURN count(trade)";
        final String valDateStr = new LocalDateConverter().toGraphProperty(valuationDate);
        final ImmutableMap<String, String> parameters = ImmutableMap.of(
                "clientId", clientId.toString(),
                "portfolioName", portfolioName.toString(),
                "valDateStr", valDateStr);
        return dao.getSession().queryForObject(Long.class, query, parameters);
    }

    @Override
    @Transactional
    public boolean isTradeValuated(ClientId clientId, TradeId tradeId, LocalDate valuationDate) {
        String query =  "MATCH (client:Client) " +
                        "-[:MANAGES]-> (:LegalEntity) " +
                        "-[:HAS]-> (:TradingAccount) " +
                        "-[:POSITIONS_ON]-> (trade:Trade) " +
                        "WHERE client.id = {clientId} " +
                        "AND trade.id = {tradeId}" +
                        "WITH trade " +
                        "MATCH (value:TradeValue {valuationDate:{date}})<-[:VALUE]-(:TradeValuation)-[:VALUATED]->(trade)" +
                        "RETURN value";
        final ImmutableMap<String, String> parameters = ImmutableMap.of(
                "clientId", clientId.toString(),
                "tradeId", tradeId.toString(),
                "date", new LocalDateConverter().toGraphProperty(valuationDate));
        Iterable<TradeValue> values = dao.getSession().query(TradeValue.class, query, parameters);
        return values != null && !Iterables.isEmpty(values);
    }

    @Override
    @Transactional
    public boolean isPortfolioValuated(ClientId clientId, PortfolioName portfolioName, Types.CallType callType, LocalDate valuationDate) {
        String query =
                "MATCH (client:Client)-[:MANAGES]-(legal:LegalEntity)-[]-(a:Agreement)<-[:FOLLOWS]-(portfolio:Portfolio) " +
                "WHERE client.id = {clientId} " +
                "AND portfolio.name = {portfolioName}" +
                "WITH portfolio " +
                "MATCH (portfolio)<-[:VALUATED]-(valuation:MarginValuation)-[:VALUE]->(value:MarginValue) " +
                "WHERE valuation.callType = {callType} " +
                "AND value.valuationDate = {date} " +
                "RETURN value";
        final ImmutableMap<String, String> parameters = ImmutableMap.of(
                "clientId", clientId.toString(),
                "portfolioName", portfolioName.toString(),
                "callType", callType.name(),
                "date", new LocalDateConverter().toGraphProperty(valuationDate));
        Iterable<MarginValue> values = dao.getSession().query(MarginValue.class, query, parameters);
        return values != null && !Iterables.isEmpty(values);
    }
}