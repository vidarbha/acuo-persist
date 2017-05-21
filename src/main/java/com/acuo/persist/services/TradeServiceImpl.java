package com.acuo.persist.services;

import com.acuo.common.typeref.TypeReference;
import com.acuo.persist.entity.IRS;
import com.acuo.persist.entity.Trade;
import com.acuo.persist.ids.ClientId;
import com.acuo.persist.ids.PortfolioId;
import com.acuo.persist.ids.TradeId;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.cypher.query.SortOrder;

import java.util.List;
import java.util.Objects;
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
        if (log.isDebugEnabled()) {
            log.debug("createOrUpdate {}",trades);
        }
        List<T> toDelete = StreamSupport.stream(trades.spliterator(), false)
                .map(trade -> find(trade.getTradeId()))
                .filter(Objects::nonNull)
                .collect(toList());
        if (!toDelete.isEmpty())
            delete(toDelete);
        return save(trades);
    }
}
