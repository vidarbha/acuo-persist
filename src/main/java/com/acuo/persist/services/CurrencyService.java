package com.acuo.persist.services;

import com.acuo.persist.entity.CurrencyEntity;
import com.opengamma.strata.basics.currency.Currency;

import java.util.Map;

public interface CurrencyService extends Service<CurrencyEntity, Long> {

    Double getFXValue(Currency currency);

    Map<Currency, Double> getAllFX();

    CurrencyEntity find(String id);

    CurrencyEntity getOrCreate(Currency currency);
}
