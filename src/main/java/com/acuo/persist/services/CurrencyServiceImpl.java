package com.acuo.persist.services;

import com.acuo.persist.entity.Currency;
import com.google.common.collect.ImmutableMap;

public class CurrencyServiceImpl extends GenericService<Currency> implements CurrencyService{

    @Override
    public Class<Currency> getEntityType() {
        return Currency.class;
    }

    @Override
    public Double getFXValue(String currencyId)
    {
        String query = "MATCH (c:Currency{id:{id}})-[r:FX_RATE]->(usd:Currency {id:'USD'}) RETURN r.fxRate";
        return  session.queryForObject(Double.class, query, ImmutableMap.of("id",currencyId));
    }
}
