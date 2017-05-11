package com.acuo.persist.services;

import com.acuo.persist.entity.CurrencyEntity;
import com.acuo.persist.entity.FXRateRelation;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.opengamma.strata.basics.currency.Currency;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FXRateRelationServiceImpl extends GenericService<FXRateRelation, Long> implements FXRateRelationService {

    private final CurrencyService currencyService;

    @Inject
    public FXRateRelationServiceImpl(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public Class<FXRateRelation> getEntityType() {
        return FXRateRelation.class;
    }

    @Override
    @Transactional
    public FXRateRelation get(Currency base, Currency counter){
        String query =
                "MATCH p=(ccy1:Currency {id:{ccy1}})-[r:FX_RATE]-(ccy2:Currency {id:{ccy2}}) " +
                "RETURN p, nodes(p), relationships(p)";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("ccy1", base.getCode(), "ccy2", counter.getCode());
        return sessionProvider.get().queryForObject(FXRateRelation.class, query, parameters);
    }

    @Override
    @Transactional
    public FXRateRelation getOrCreate(Currency base, Currency counter){
        FXRateRelation fxRateRelation = get(base, counter);
        if (fxRateRelation == null) {
            CurrencyEntity baseEntity = currencyService.getOrCreate(base);
            CurrencyEntity counterEntity = currencyService.getOrCreate(counter);
            fxRateRelation = new FXRateRelation();
            fxRateRelation.setFrom(baseEntity);
            fxRateRelation.setTo(counterEntity);
            baseEntity.setFxRateRelation(fxRateRelation);
            fxRateRelation = save(fxRateRelation);
        }
        return fxRateRelation;
    }
}
