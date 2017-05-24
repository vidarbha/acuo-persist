package com.acuo.persist.services;

import com.acuo.persist.entity.FXRate;
import com.acuo.persist.entity.FXValue;
import com.opengamma.strata.basics.currency.Currency;

import java.time.LocalDateTime;

public interface FXRateService extends Service<FXRate, Long> {

    FXRate get(Currency base, Currency counter);

    FXRate getOrCreate(Currency base, Currency counter);

    void addValue(FXRate fxRate, Double value, LocalDateTime updated);
}
