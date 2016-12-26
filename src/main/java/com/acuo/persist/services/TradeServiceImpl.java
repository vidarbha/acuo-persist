package com.acuo.persist.services;

import com.acuo.common.typeref.TypeReference;
import com.acuo.persist.entity.Entity;
import com.acuo.persist.entity.Trade;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

import javax.inject.Singleton;

@Singleton
@Transactional
public class TradeServiceImpl<T extends Trade> extends GenericService<T> implements TradeService<T> {

    @Override
    public Iterable<T> findBilateralTradesByClientId(String clientId) {
        /*String query =  "MATCH (c:Client {id:{id}})-[:MANAGES]->(le:LegalEntity)-[:HAS]->(:TradingAccount)-[:POSITIONS_ON]->(t:Trade) " +
                        "MATCH (t)-[:FOLLOWS]->(a:Agreement) " +
                        "WHERE a.type='bilateralOTC' RETURN t";*/
        String query =  "MATCH (:Client {id:{id}}) " +
                        "-[:MANAGES]-> (:LegalEntity) " +
                        "-[:HAS]-> (:TradingAccount) " +
                        "-[:POSITIONS_ON]-> (trade:Trade) " +
                        "WITH trade " +
                        "MATCH p=(trade)-[r*0..1]-() RETURN trade, nodes(p), rels(p)";
        return session.query(getEntityType(), query, ImmutableMap.of("id",clientId));
    }

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

}
