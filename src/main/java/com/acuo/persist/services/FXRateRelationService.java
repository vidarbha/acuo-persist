package com.acuo.persist.services;

import com.acuo.persist.entity.FXRateRelation;
import com.opengamma.strata.basics.currency.Currency;

public interface FXRateRelationService extends Service<FXRateRelation, Long> {

    FXRateRelation get(Currency base, Currency counter);

    FXRateRelation getOrCreate(Currency base, Currency counter);

}
