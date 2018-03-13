package com.acuo.persist.services;

import com.acuo.persist.entity.CurrencyEntity;
import com.acuo.persist.entity.FXRate;
import com.acuo.persist.entity.FXValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.inject.persist.Transactional;
import com.opengamma.strata.basics.currency.Currency;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FXRateServiceImpl extends AbstractService<FXRate, Long> implements FXRateService {

    private final CurrencyService currencyService;
    private final FXValueService fxValueService;

    @Inject
    public FXRateServiceImpl(Provider<Session> session,
                             CurrencyService currencyService,
                             FXValueService fxValueService) {
        super(session);
        this.currencyService = currencyService;
        this.fxValueService = fxValueService;
    }

    @Override
    public Class<FXRate> getEntityType() {
        return FXRate.class;
    }

    @Override
    @Transactional
    public FXRate get(Currency base, Currency counter){
        String query =
                "MATCH (ccy1:Currency {id:{ccy1}})--(fxRate:FXRate)--(ccy2:Currency {id:{ccy2}}) " +
                "MATCH p=(fxRate)-[*0..1]-()" +
                "RETURN p, nodes(p), relationships(p)";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("ccy1", base.getCode(), "ccy2", counter.getCode());
        return dao.getSession().queryForObject(FXRate.class, query, parameters);
    }

    @Transactional
    @Override
    public Double getFXValue(Currency currency) {
        if (Currency.USD.equals(currency)) return 1d;
        final FXRate fxRate = get(currency, Currency.USD);
        final Currency base = Currency.of(fxRate.getFrom().getCurrencyId());
        final FXValue fxValue = fxRate.getLast();
        final Double rate = fxValue.getValue();
        return Currency.USD.equals(base) ? 1 / rate : rate;
    }

    @Transactional
    @Override
    public Map<Currency, Double> getAllFX() {
        Map<Currency, Double> values = new HashMap<>();
        values.put(Currency.USD, 1d);
        String query =
                "MATCH (from:Currency)<-[:FROM]-(fxRate:FXRate)-[:TO]->(to:Currency) " +
                        "MATCH (fxRate)-[:LAST]->(fxValue:FXValue) " +
                        "RETURN fxValue.value as rate, from.id as from, to.id as to";
        Result result = dao.getSession().query(query, Collections.emptyMap());
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

    @Override
    @Transactional
    public FXRate getOrCreate(Currency base, Currency counter){
        FXRate fxRate = get(base, counter);
        if (fxRate == null) {
            CurrencyEntity baseEntity = currencyService.getOrCreate(base);
            CurrencyEntity counterEntity = currencyService.getOrCreate(counter);
            fxRate = new FXRate();
            fxRate.setFrom(baseEntity);
            fxRate.setTo(counterEntity);
            fxRate = save(fxRate);
        }
        return fxRate;
    }

    @Override
    @Transactional
    public void addValue(FXRate fxRate, Double value, LocalDateTime updated){
        cleanup(fxRate);
        FXValue fxValue = new FXValue();
        fxValue.setFrom(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        fxValue.setLastUpdate(updated);
        fxValue.setValue(value);
        fxValue.setRate(fxRate);
        FXValue previous = fxRate.getLast();
        fxValue.setPrevious(previous);
        fxValue = fxValueService.save(fxValue, 2);
        fxRate.setLast(fxValue);
        save(fxRate);
    }

    private void cleanup(FXRate fxRate) {
        final long twentyFourHours = LocalDateTime.now().minusHours(24).toEpochSecond(ZoneOffset.UTC);
        String query =
                "MATCH (fxRate:FXRate)<-[:OF]-(value:FXValue) " +
                "WHERE ID(fxRate)={id} AND value.from < {time} " +
                "RETURN value";
        final ImmutableMap<String, Long> parameters = ImmutableMap.of("id", fxRate.getId(), "time", twentyFourHours);
        final Iterable<FXValue> fxValues = dao.getSession().query(FXValue.class, query, parameters);
        if (!Iterables.isEmpty(fxValues))
            fxValueService.delete(fxValues);
    }
}
