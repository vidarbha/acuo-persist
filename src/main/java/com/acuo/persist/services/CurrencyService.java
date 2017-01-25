package com.acuo.persist.services;

import com.acuo.persist.entity.Currency;

public interface CurrencyService extends Service<Currency> {

    public Double getFXValue(String currencyId);
}
