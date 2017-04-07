package com.acuo.persist.services;

import com.acuo.persist.entity.CurrencyEntity;

import java.util.Map;

public interface CurrencyService extends Service<CurrencyEntity> {

    public Double getFXValue(String currencyId);

    public Map<String, Double> getAllFX();
}
