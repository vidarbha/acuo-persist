package com.acuo.persist.services;

import com.acuo.persist.entity.Currency;

import java.util.Map;

public interface CurrencyService extends Service<Currency> {

    public Double getFXValue(String currencyId);

    public Map<String, Double> getAllFX();
}
