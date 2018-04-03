package com.acuo.persist.services;

import com.acuo.persist.entity.FXRate;
import com.opengamma.strata.basics.currency.Currency;

import java.time.LocalDateTime;
import java.util.Map;

public interface FXRateService extends Service<FXRate, Long> {

    FXRate get(Currency base, Currency counter);

    Double getFXValue(Currency currency);

    Map<Currency, FXRate> getAllFX();

    FXRate getOrCreate(Currency base, Currency counter);

    void addValue(FXRate fxRate, Double value, LocalDateTime refreshed, LocalDateTime updated);
}
