package com.acuo.persist.services;

import com.acuo.persist.entity.CurrencyEntity;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.model.Result;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CurrencyServiceImpl extends GenericService<CurrencyEntity> implements CurrencyService {

    @Override
    public Class<CurrencyEntity> getEntityType() {
        return CurrencyEntity.class;
    }

    @Transactional
    @Override
    public Double getFXValue(String currencyId) {
        String query = "MATCH (c:CurrencyEntity{id:{id}})-[r:FX_RATE]->(usd:CurrencyEntity {id:'USD'}) RETURN r.fxRate";
        return sessionProvider.get().queryForObject(Double.class, query, ImmutableMap.of("id", currencyId));
    }

    @Transactional
    @Override
    public Map<String, Double> getAllFX()
    {
        Map<String, Double> values = new HashMap<String, Double>();
        String query = "MATCH (c:CurrencyEntity)-[r:FX_RATE]->(usd:CurrencyEntity {id:'USD'}) RETURN c.id as id, r.fxRate as rate";
        Result result = sessionProvider.get().query(query, Collections.emptyMap());
        result.forEach(map -> values.put((String)map.get("id"), (Double)map.get("rate")));
        return values;
    }
}
