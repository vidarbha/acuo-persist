package com.acuo.persist.services;

import com.acuo.persist.entity.Trade;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

import javax.inject.Singleton;

@Singleton
public class TradeServiceImpl extends GenericService<Trade> implements TradeService {

    @Override
    public Class<Trade> getEntityType() {
        return Trade.class;
    }

    @Transactional
    @Override
    public Iterable<Trade> findByClientId(String id) {
        String query = "match (trade:Trade {id: {id} }) return i";
        return session.query(getEntityType(), query, ImmutableMap.of("id",id));
    }
}
