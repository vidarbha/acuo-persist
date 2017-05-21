package com.acuo.persist.services;

import com.acuo.persist.entity.CurrencyEntity;
import com.acuo.persist.entity.FXRate;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.opengamma.strata.basics.currency.Currency;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FXRateServiceImpl extends GenericService<FXRate, Long> implements FXRateService {

    private final CurrencyService currencyService;

    @Inject
    public FXRateServiceImpl(CurrencyService currencyService) {
        this.currencyService = currencyService;
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
}
