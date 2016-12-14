package com.acuo.persist.services;

import com.acuo.persist.entity.IRS;
import com.acuo.persist.entity.Trade;
import com.google.common.collect.ImmutableMap;

import javax.inject.Singleton;

@Singleton
public class TradeServiceImpl extends GenericService<Trade> implements TradeService {

    @Override
    public Class<Trade> getEntityType() {
        return Trade.class;
    }

    @Override
    public Trade findById(String id) {
        String query = "match (i:Trade {id: {id} }) return i";
        return session.queryForObject(IRS.class, query, ImmutableMap.of("id",id));
    }
}
