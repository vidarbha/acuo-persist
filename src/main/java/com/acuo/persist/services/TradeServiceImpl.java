package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioName;
import com.acuo.common.ids.TradeId;
import com.acuo.common.typeref.TypeReference;
import com.acuo.persist.entity.trades.Trade;
import com.google.common.collect.Iterables;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.stream.Collectors.toList;

@Slf4j
public class TradeServiceImpl<T extends Trade> extends AbstractService<T, TradeId> implements TradeService<T> {

    @Inject
    public TradeServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<T> getEntityType() {
        TypeReference<T> ref = i->i;
        return ref.type();
    }

    @Override
    @Transactional
    public T findTradeBy(ClientId clientId, TradeId tradeId) {
        if (log.isDebugEnabled()) {
            log.debug("findTradeBy clientId {} and tradeId {}",clientId, tradeId);
        }
        String query =  "MATCH (client:Client) " +
                        "-[:MANAGES]-> (:LegalEntity) " +
                        "-[:HAS]-> (:TradingAccount) " +
                        "-[:POSITIONS_ON]-> (trade:Trade) " +
                        "WHERE client.id = {clientId} " +
                        "AND trade.id = {tradeId}" +
                        "WITH trade " +
                        "MATCH p=(trade)-[*0..1]-() RETURN p";
        return dao.getSession().queryForObject(getEntityType(), query, of(
                "clientId", clientId.toString(),
                "tradeId", tradeId
                ));
    }

    @Override
    @Transactional
    public Iterable<T> findAllClientTrades(ClientId clientId) {
        if (log.isDebugEnabled()) {
            log.debug("findAllClientTrades clientId {}",clientId);
        }
        String query =  "MATCH (client:Client) " +
                        "-[:MANAGES]-> (:LegalEntity) " +
                        "-[:HAS]-> (:TradingAccount) " +
                        "-[:POSITIONS_ON]-> (trade:Trade) " +
                        "WHERE client.id = {clientId} " +
                        "WITH trade " +
                        "MATCH p=(trade)-[*0..1]-() RETURN p";
        return dao.getSession().query(getEntityType(), query, of("clientId", clientId.toString()));
    }

    @Override
    @Transactional
    public Iterable<T> findByPortfolioId(ClientId clientId, PortfolioName... portfolioNames) {
        if (log.isDebugEnabled()) {
            log.debug("findByPortfolioId clientId {} for {}", clientId, Arrays.asList(portfolioNames));
        }
        String query =
                "MATCH p=()-[*0..1]-(t:Trade)-[r:BELONGS_TO]->(portfolio:Portfolio)-[:FOLLOWS]->(a:Agreement) " +
                "<-[]-(:LegalEntity)-[:MANAGES]-(client:Client) " +
                        "WHERE client.id = {clientId} AND portfolio.name IN {names} " +
                "RETURN p";
        return dao.getSession().query(getEntityType(), query, of(
                "clientId",clientId.toString(),
                "names", portfolioNames)
                );
    }

    @Override
    @Transactional
    public Iterable<T> findAllTradeByIds(ClientId clientId, List<TradeId> tradeIds) {
        if (log.isDebugEnabled()) {
            log.debug("findAllTradeByIds clientId {} and tradeIds {} ", clientId, tradeIds);
        }
        String query =  "MATCH (client:Client) " +
                        "-[:MANAGES]-> (:LegalEntity) " +
                        "-[:HAS]-> (:TradingAccount) " +
                        "-[:POSITIONS_ON]-> (trade:Trade) " +
                        "WHERE client.id = {clientId} " +
                        "AND trade.id IN {tradeIds} " +
                        "WITH trade " +
                        "MATCH p=(trade)-[*0..1]-() RETURN p";
        return dao.getSession().query(getEntityType(), query, of(
                "clientId", clientId.toString(),
                "tradeIds", tradeIds));
    }

    @Transactional
    public T createOrUpdate(T trade) {
        if (log.isDebugEnabled()) {
            log.debug("createOrUpdate {}",trade);
        }
        T byId = find(trade.getTradeId());
        if(byId != null) {
            delete(byId);
        }
        return save(trade);
    }

    @Transactional
    public <S extends T> Iterable<S> createOrUpdate(ClientId clientId, Iterable<S> trades) {
        long start = System.nanoTime();
        final Iterable<S> saved = save(trades, 1);
        long end = System.nanoTime();
        log.info("Save time: " + TimeUnit.NANOSECONDS.toSeconds(end - start));
        final Iterable<T> all = findAllClientTrades(clientId);
        final List<TradeId> idsToDelete = intersection(tradeIds(all), tradeIds(saved));
        Iterable<T> toDelete = findAllTradeByIds(clientId, idsToDelete);
        if (!Iterables.isEmpty(toDelete)) {
            delete(toDelete);
        }

        if (log.isDebugEnabled()) {
            log.debug("saving {} trades",Iterables.size(trades));
            log.debug("saved {} trades",Iterables.size(saved));
            log.debug("found {} trades",Iterables.size(all));
            log.debug("deleted {} trades",Iterables.size(toDelete));
        }
        return saved;
    }

    private <S extends T> List<TradeId> tradeIds(Iterable<S> trades) {
        return StreamSupport.stream(trades.spliterator(), false)
                .map(Trade::getTradeId)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private List<TradeId> intersection(List<TradeId> right, List<TradeId> left) {
        final Predicate<TradeId> contains = left::contains;
        return right.stream()
                .filter(contains.negate())
                .collect(toList());
    }
}
