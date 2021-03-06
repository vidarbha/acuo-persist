package com.acuo.persist.services;

import com.acuo.persist.entity.CurrencyEntity;
import com.acuo.persist.entity.FXRate;
import com.acuo.persist.entity.FXValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.inject.persist.Transactional;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.currency.FxRate;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * Get the FX rate in units of given currency for one USD
     */
    @Transactional
    @Override
    public Double getFXValue(Currency currency) {
        if (Currency.USD.equals(currency)) return 1d;
        final FXRate fxRate = get(currency, Currency.USD);
        final FXValue fxValue = fxRate.getLast();
        final String fromCurrency = fxRate.getFrom().getCurrencyId();
        if (Currency.USD.toString().equals(fromCurrency))
            return fxValue.getValue();
        else
            return 1 / fxValue.getValue();
    }

    @Transactional
    @Override
    public Map<Currency, FXRate> getAllFX() {
        Map<Currency, FXRate> values = new HashMap<>();
        values.put(Currency.USD, FXRate.USD_RATE);
        String query =
                "MATCH p=(from:Currency)<-[:FROM]-(fxRate:FXRate)-[:TO]->(to:Currency) " +
                "MATCH v=(fxRate)-[:LAST]->(fxValue:FXValue) " +
                "RETURN p, v";
        final Iterable<FXRate> results = dao.getSession().query(FXRate.class, query, Collections.emptyMap());
        results.forEach(rate -> {
            final Currency from = Currency.of(rate.getFrom().getCurrencyId());
            final Currency to = Currency.of(rate.getTo().getCurrencyId());
            if (Currency.USD.equals(from))
                values.put(to, rate);
            else if (Currency.USD.equals(to))
                values.put(from, rate);
        });
        return values;
    }

    @Override
    @Transactional
    public FXRate save(FxRate fxRate, LocalDateTime lastUpdate) {
        final Currency base = fxRate.getPair().getBase();
        final Currency counter = fxRate.getPair().getCounter();
        FXRate fx = getOrCreate(base, counter);
        double value = fxRate.fxRate(fxRate.getPair());
        LocalDateTime refreshed = LocalDateTime.now();
        return addValue(fx, value, refreshed, lastUpdate);
    }

    private FXRate getOrCreate(Currency base, Currency counter){
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

    private FXRate addValue(FXRate fxRate, Double value, LocalDateTime refreshed, LocalDateTime updated){
        cleanup(fxRate);
        FXValue fxValue = new FXValue();
        fxValue.setFrom(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        fxValue.setRefreshedOn(refreshed);
        fxValue.setLastUpdate(updated);
        fxValue.setValue(value);
        fxValue.setRate(fxRate);
        FXValue previous = fxRate.getLast();
        fxValue.setPrevious(previous);
        fxValue = fxValueService.save(fxValue, 2);
        fxRate.setLast(fxValue);
        return save(fxRate);
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
