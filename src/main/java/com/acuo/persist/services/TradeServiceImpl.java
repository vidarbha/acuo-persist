package com.acuo.persist.services;

import com.acuo.persist.entity.Trade;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

import javax.inject.Singleton;

@Singleton
@Transactional
public class TradeServiceImpl extends GenericService<Trade> implements TradeService {


    @Override
    public Iterable<Trade> findByClientId(String id) {
        String query = "match (trade:Trade {id: {id} }) return i";
        return session.query(getEntityType(), query, ImmutableMap.of("id",id));
    }

    @Override
    public Iterable<Trade> findBilateralTradesByClientId(String clientId) {
        /*String query =  "MATCH (c:Client {id:{id}})-[:MANAGES]->(le:LegalEntity)-[:HAS]->(:Account)-[:POSITIONS_ON]->(t:Trade) " +
                        "MATCH (t)-[:FOLLOWS]->(a:Agreement) " +
                        "WHERE a.type='bilateralOTC' RETURN t";*/
        String query =  "MATCH (c:Client {id:{id}}) -[:MANAGES]-> (le:LegalEntity) -[:HAS]-> (:Account) -[:POSITIONS_ON]-> (t:Trade) return t";
        return session.query(getEntityType(), query, ImmutableMap.of("id",clientId));
    }

    @Override
    public Class<Trade> getEntityType() {
        return Trade.class;
    }

}
