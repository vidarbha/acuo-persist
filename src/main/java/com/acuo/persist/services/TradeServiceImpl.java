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

public class TradeServiceImpl<T extends Trade> extends GenericService<T> implements TradeService<T> {

    @Override
    public Iterable<T> findBilateralTradesByClientId(ClientId clientId) {
        String query =  "MATCH (:Client {id:{id}}) " +
                        "-[:MANAGES]-> (:LegalEntity) " +
                        "-[:HAS]-> (:TradingAccount) " +
                        "-[:POSITIONS_ON]-> (trade:Trade) " +
                        "WITH trade " +
                        "MATCH p=(trade)-[r*0..1]-() RETURN trade, nodes(p), rels(p)";
        return session.query(getEntityType(), query, ImmutableMap.of("id",clientId.toString()));
    }

    @Override
    public Class<T> getEntityType() {
        TypeReference<T> ref = i->i;
        return ref.type();
    }

    @Override
    public Iterable<T> findByPortfolioId(PortfolioId portfolioId) {
        String query =  "MATCH (t:IRS)-[r:BELONGS_TO]->(p:Portfolio {id:{id}}) return t";
        return session.query(getEntityType(), query, ImmutableMap.of("id",portfolioId.toString()));
    }

    @Override
    public Iterable<T> findAllIRS() {
        String query =  "MATCH (n:IRS {tradeType:'Bilateral'}) RETURN n";
        return session.query(getEntityType(), query, Collections.emptyMap());
    }

    public <S extends T> S createOrUpdate(S trade) {
        T byId = findById(trade.getTradeId());
        if(byId != null) {
            delete(byId);
        }
        return save(trade);
    }

    public <S extends T> Iterable<S> createOrUpdate(Iterable<S> trades) {
        for (S trade: trades) {
            T byId = findById(trade.getTradeId());
            if (byId != null) {
                delete(byId);
            }
        }
        return save(trades);
    }
}
