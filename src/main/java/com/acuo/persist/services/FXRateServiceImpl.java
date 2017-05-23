package com.acuo.persist.services;

import com.acuo.persist.entity.CurrencyEntity;
import com.acuo.persist.entity.FXRate;
import com.acuo.persist.entity.FXValue;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.opengamma.strata.basics.currency.Currency;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
public class FXRateServiceImpl extends GenericService<FXRate, Long> implements FXRateService {

    private final CurrencyService currencyService;
    private final FXValueService fxValueService;

    @Inject
    public FXRateServiceImpl(CurrencyService currencyService, FXValueService fxValueService) {
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
                "MATCH p=(ccy1:Currency {id:{ccy1}})--(fxRate:FXRate)--(ccy2:Currency {id:{ccy2}}) " +
                "MATCH (fxRate)<-[:OF]-(value:FXValue) " +
                "RETURN p, nodes(p), relationships(p)";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("ccy1", base.getCode(), "ccy2", counter.getCode());
        return sessionProvider.get().queryForObject(FXRate.class, query, parameters);
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
        fxValue.setPrevious(latestValueOf(fxRate));
        fxValueService.save(fxValue);
    }

    @Override
    @Transactional
    public FXValue latestValueOf(FXRate fxRate) {
        String query =
                "MATCH (fxRate:FXRate)<-[:OF]-(value:FXValue) " +
                "WHERE ID(fxRate)={id} " +
                "RETURN value ORDER BY value.from DESC LIMIT 1";
        final ImmutableMap<String, Long> parameters = ImmutableMap.of("id", fxRate.getId());
        return sessionProvider.get().queryForObject(FXValue.class, query, parameters);
    }

    private void cleanup(FXRate fxRate) {
        final long twentyFourHours = LocalDateTime.now().minusHours(24).toEpochSecond(ZoneOffset.UTC);
        String query =
                "MATCH (fxRate:FXRate)<-[:OF]-(value:FXValue) " +
                "WHERE ID(fxRate)={id} AND value.from < {time} " +
                "RETURN value";
        final ImmutableMap<String, Long> parameters = ImmutableMap.of("id", fxRate.getId(), "time", twentyFourHours);
        final Iterable<FXValue> fxValues = sessionProvider.get().query(FXValue.class, query, parameters);
        if (!Iterables.isEmpty(fxValues))
            fxValueService.delete(fxValues);
    }
}
