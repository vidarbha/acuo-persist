package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioId;
import com.acuo.common.ids.TradeId;
import com.acuo.common.typeref.TypeReference;
import com.acuo.persist.entity.IRS;
import com.acuo.persist.entity.Trade;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Slf4j
public class TradeServiceImpl<T extends Trade> extends AbstractService<T, TradeId> implements TradeService<T> {

    @Inject
    public TradeServiceImpl(Provider<Session> session) {
        super(session);
    }

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
        return dao.getSession().query(getEntityType(), query, ImmutableMap.of("id",clientId.toString()));
    }

    @Override
    public Class<T> getEntityType() {
        TypeReference<T> ref = i->i;
        return ref.type();
    }

    @Override
    @Transactional
    public T find(TradeId id) {
        String query =
                "MATCH p=()-[*0..1]-(t:Trade {id:{id}}) RETURN p";
        return dao.getSession().queryForObject(getEntityType(), query, ImmutableMap.of("id", id));
    }

    @Override
    @Transactional
    public Iterable<T> findByPortfolioId(PortfolioId... ids) {
        if (log.isDebugEnabled()) {
            log.debug("findByPortfolioId for {}", Arrays.asList(ids));
        }
        String query =
                "MATCH p=()-[*0..1]-(t:Trade)-[r:BELONGS_TO]->(portfolio:Portfolio)-[:FOLLOWS]->(a:Agreement) " +
                "WHERE portfolio.id IN {ids} " +
                "RETURN p, nodes(p), relationships(p)";
        return dao.getSession().query(getEntityType(), query, ImmutableMap.of("ids", ids));
    }

    @Override
    @Transactional
    public Iterable<IRS> findAllIRS() {
        if (log.isDebugEnabled()) {
            log.debug("findAllIRS");
        }
        return dao.getSession().loadAll(IRS.class, new SortOrder().add("tradeType"),1);
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
        return dao.getSession().query(getEntityType(), query, ImmutableMap.of("ids", ids));
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
        long start = System.nanoTime();
        final Iterable<S> saved = save(trades, 1);
        long end = System.nanoTime();
        log.info("Save time: " + TimeUnit.NANOSECONDS.toSeconds(end - start));
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
