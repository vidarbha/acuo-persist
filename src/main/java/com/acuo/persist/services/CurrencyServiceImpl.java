package com.acuo.persist.services;

import com.acuo.persist.entity.CurrencyEntity;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.opengamma.strata.basics.currency.Currency;

import java.util.Map;

public class CurrencyServiceImpl extends GenericService<CurrencyEntity, String> implements CurrencyService {

    private final FXRateService fxRateService;

    @Inject
    public CurrencyServiceImpl(FXRateService fxRateService) {
        this.fxRateService = fxRateService;
    }

    @Override
    public Class<CurrencyEntity> getEntityType() {
        return CurrencyEntity.class;
    }

    @Transactional
    @Override
    @Deprecated
    /**
     * @deprecated use {@link FXRateService#getFXValue(Currency)}
     */
    public Double getFXValue(Currency currency) {
        return fxRateService.getFXValue(currency);
    }

    @Transactional
    @Override
    @Deprecated
    /**
     * @deprecated use {@link FXRateService#getAllFX()}
     */
    public Map<Currency, Double> getAllFX() {
        return fxRateService.getAllFX();
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
