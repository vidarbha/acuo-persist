package com.acuo.persist.services;

import com.acuo.common.typeref.TypeReference;
import com.acuo.persist.entity.IRS;
import com.acuo.persist.entity.Trade;
import com.acuo.persist.ids.ClientId;
import com.acuo.persist.ids.PortfolioId;
import com.acuo.persist.ids.TradeId;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.cypher.query.SortOrder;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Slf4j
public class TradeServiceImpl<T extends Trade> extends GenericService<T, TradeId> implements TradeService<T> {

    @Override
    @Transactional
    public Iterable<T> findBilateralTradesByClientId(ClientId clientId) {
        if (log.isDebugEnabled()) {
            log.debug("findBilateralTradesByClientId {}",clientId);
        }
        String query =  "MATCH (:Client {id:{id}}) " +
                        "-[:MANAGES]-> (:LegalEntity) " +
                        "-[:HAS]-> (:TradingAccount) " +
                        "-[:POSITIONS_ON]-> (trade:Trade) " +
                        "WITH trade " +
                        "MATCH p=(trade)-[r*0..1]-() RETURN trade, nodes(p), relationships(p)";
        return sessionProvider.get().query(getEntityType(), query, ImmutableMap.of("id",clientId.toString()));
    }

    @Override
    public Class<T> getEntityType() {
        TypeReference<T> ref = i->i;
        return ref.type();
    }

    @Override
    @Transactional
    public Iterable<T> findByPortfolioId(PortfolioId portfolioId) {
        if (log.isDebugEnabled()) {
            log.debug("findByPortfolioId {}",portfolioId);
        }
        String query =  "MATCH (t:IRS)-[r:BELONGS_TO]->(p:Portfolio {id:{id}}) return t";
        return sessionProvider.get().query(getEntityType(), query, ImmutableMap.of("id",portfolioId.toString()));
    }

    @Override
    @Transactional
    public Iterable<IRS> findAllIRS() {
        if (log.isDebugEnabled()) {
            log.debug("findAllIRS");
        }
        return sessionProvider.get().loadAll(IRS.class, new SortOrder().add("tradeType"),1);
    }

    @Override
    @Transactional
    public Iterable<T> findAllTradeByIds(List<TradeId> ids) {
        if (log.isDebugEnabled()) {
            log.debug("findAllTradeByIds {} ", ids);
        }
        String query =
                "MATCH p=(t:Trade)-[*0..1]-() " +
                "WHERE t.id IN {ids}" +
                "RETURN p, nodes(p), relationships(p)";
        return sessionProvider.get().query(getEntityType(), query, ImmutableMap.of("ids", ids));
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
    public <S extends T> Iterable<S> createOrUpdate(Iterable<S> trades) {
        final Iterable<S> saved = save(trades);
        final Iterable<T> all = findAll();
        final List<TradeId> idsToDelete = intersection(tradeIds(all), tradeIds(saved));
        Iterable<T> toDelete = findAllTradeByIds(idsToDelete);
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
