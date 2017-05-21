package com.acuo.persist.services;

import com.acuo.common.util.ArgChecker;
import com.acuo.persist.entity.CurrencyEntity;
import com.acuo.persist.entity.FXRate;
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
        if (Currency.USD.equals(currency)) return 1d;
        String query =
                "MATCH p=(c:Currency{id:{id}})--(fxRate:FXRate)--(usd:Currency {id:'USD'}) " +
                "RETURN p, nodes(p), relationships(p)";
        final FXRate fxRate = sessionProvider.get().queryForObject(FXRate.class, query, ImmutableMap.of("id", currency.getCode()));
        final Currency base = Currency.of(fxRate.getFrom().getCurrencyId());
        final Double rate = fxRate.getValue();
        return Currency.USD.equals(base) ? 1 / rate : rate;
    }

    @Transactional
    @Override
    public Map<Currency, Double> getAllFX() {
        Map<Currency, Double> values = new HashMap<>();
        values.put(Currency.USD, 1d);
        String query =
                "MATCH (from:Currency)<-[:FROM]-(fxRate:FXRate)-[:TO]->(to:Currency) " +
                "RETURN fxRate.value as rate, from.id as from, to.id as to";
        Result result = sessionProvider.get().query(query, Collections.emptyMap());
        result.forEach(map -> {
            final Double rate = (Double) map.get("rate");
            final Currency from = Currency.of((String) map.get("from"));
            final Currency to = Currency.of((String) map.get("to"));
            if (Currency.USD.equals(from))
                values.put(to, 1/rate);
            else if (Currency.USD.equals(to))
                values.put(from, rate);
        });
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

    @Transactional
    @Override
    public CurrencyEntity getOrCreate(Currency currency) {
        CurrencyEntity currencyEntity = find(currency.getCode());
        if (currencyEntity == null) {
            currencyEntity = new CurrencyEntity();
            currencyEntity.setCurrencyId(currency.getCode());
            currencyEntity.setName(currency.getCode());
            currencyEntity = save(currencyEntity);
        }
        return currencyEntity;
    }
}
