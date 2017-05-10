package com.acuo.persist.services;

import com.acuo.common.util.ArgChecker;
import com.acuo.persist.entity.CurrencyEntity;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import com.opengamma.strata.basics.currency.Currency;
import org.neo4j.ogm.model.Result;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CurrencyServiceImpl extends GenericService<CurrencyEntity, Long> implements CurrencyService {

    @Override
    public Class<CurrencyEntity> getEntityType() {
        return CurrencyEntity.class;
    }

    @Transactional
    @Override
    public Double getFXValue(Currency currency) {
        String query =
                "MATCH (c:Currency{id:{id}})-[r:FX_RATE]-(usd:Currency {id:'USD'}) " +
                "RETURN CASE WHEN STARTNODE(r) = c THEN r.fxRate ELSE 1/r.fxRate END";
        return sessionProvider.get().queryForObject(Double.class, query, ImmutableMap.of("id", currency.getCode()));
    }

    @Transactional
    @Override
    public Map<Currency, Double> getAllFX() {
        Map<Currency, Double> values = new HashMap<>();
        String query =
                "MATCH (c:Currency)-[r:FX_RATE]-(usd:Currency {id:'USD'}) " +
                "RETURN c.id as id, CASE WHEN STARTNODE(r) = c THEN r.fxRate ELSE 1/r.fxRate END AS rate " +
                "ORDER BY id";
        Result result = sessionProvider.get().query(query, Collections.emptyMap());
        result.forEach(map -> values.put(Currency.of((String) map.get("id")), (Double) map.get("rate")));
        return values;
    }

    @Transactional
    @Override
    public CurrencyEntity find(String id) {
        ArgChecker.notNull(id, "id");
        String query = "MATCH (i:Currency {id: {id} }) return i";
        CurrencyEntity entity = sessionProvider.get().queryForObject(CurrencyEntity.class, query, ImmutableMap.of("id",id));
        if(entity != null)
            return find(entity.getId(), 1);
        else
            return null;
    }
}
