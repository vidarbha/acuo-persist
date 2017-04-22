package com.acuo.persist.services;

import com.acuo.common.typeref.TypeReference;
import com.acuo.persist.entity.Trade;
import com.acuo.persist.ids.ClientId;
import com.acuo.persist.ids.PortfolioId;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class TradeServiceImpl<T extends Trade> extends GenericService<T> implements TradeService<T> {

    @Override
    @Transactional
    public Iterable<T> findBilateralTradesByClientId(ClientId clientId) {
        String query =  "MATCH (:Client {id:{id}}) " +
                        "-[:MANAGES]-> (:LegalEntity) " +
                        "-[:HAS]-> (:TradingAccount) " +
                        "-[:POSITIONS_ON]-> (trade:Trade) " +
                        "WITH trade " +
                        "MATCH p=(trade)-[r*0..1]-() RETURN trade, nodes(p), rels(p)";
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
        String query =  "MATCH (t:IRS)-[r:BELONGS_TO]->(p:Portfolio {id:{id}}) return t";
        return sessionProvider.get().query(getEntityType(), query, ImmutableMap.of("id",portfolioId.toString()));
    }

    @Override
    @Transactional
    public Iterable<T> findAllIRS() {
        String query =  "MATCH (n:IRS {tradeType:'Bilateral'}) RETURN n";
        return sessionProvider.get().query(getEntityType(), query, Collections.emptyMap());
    }

    public <S extends T> S createOrUpdate(S trade) {
        T byId = findById(trade.getTradeId());
        if(byId != null) {
            delete(byId);
        }
        return save(trade);
    }

    public <S extends T> Iterable<S> createOrUpdate(Iterable<S> trades) {
        List<T> toDelete = StreamSupport.stream(trades.spliterator(), true)
                .map(trade -> findById(trade.getTradeId()))
                .filter(trade -> trade != null)
                .collect(toList());
        if (!toDelete.isEmpty())
            delete(toDelete);
        return save(trades);
    }
}
