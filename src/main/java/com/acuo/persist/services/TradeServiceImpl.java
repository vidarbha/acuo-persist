package com.acuo.persist.services;

import com.acuo.common.typeref.TypeReference;
import com.acuo.persist.entity.Trade;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.Iterator;

@Singleton
@Transactional
public class TradeServiceImpl<T extends Trade> extends GenericService<T> implements TradeService<T> {

    @Override
    public Iterable<T> findBilateralTradesByClientId(String clientId) {
        String query =  "MATCH (:Client {id:{id}}) " +
                        "-[:MANAGES]-> (:LegalEntity) " +
                        "-[:HAS]-> (:TradingAccount) " +
                        "-[:POSITIONS_ON]-> (trade:Trade) " +
                        "WITH trade " +
                        "MATCH p=(trade)-[r*0..1]-() RETURN trade, nodes(p), rels(p)";
        return session.query(getEntityType(), query, ImmutableMap.of("id",clientId));
    }

    @Override
    public T findById(Long id) {
        String query =  "MATCH (trade:Trade {tradeId: {id} }) " +
                        "WITH trade " +
                        "MATCH p=(trade)-[r*0..1]-() RETURN trade, nodes(p), rels(p)";
        return session.queryForObject(getEntityType(), query, ImmutableMap.of("id",id));
    }

    @Override
    public Class<T> getEntityType() {
        TypeReference<T> ref = i->i;
        return ref.type();
    }

    @Override
    public Iterable<T> findByPortfolioId(String portfolioId)
    {
        String query =  "MATCH (t:IRS)-[r:BELONGS_TO]->(p:Portfolio {id:{id}}) return t";
        return session.query(getEntityType(), query, ImmutableMap.of("id",portfolioId));
    }

    @Override
    public Iterable<T> findAllIRS()
    {
        String query =  "MATCH (n:IRS) RETURN n";
        return session.query(getEntityType(), query, Collections.emptyMap());
    }

}
