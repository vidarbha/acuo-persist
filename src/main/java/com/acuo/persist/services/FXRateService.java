package com.acuo.persist.services;

import com.acuo.persist.entity.FXRate;
import com.opengamma.strata.basics.currency.Currency;

public interface FXRateService extends Service<FXRate, Long> {

    FXRate get(Currency base, Currency counter);

    FXRate getOrCreate(Currency base, Currency counter);

}
