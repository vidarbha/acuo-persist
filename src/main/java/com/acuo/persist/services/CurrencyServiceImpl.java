package com.acuo.persist.services;

import com.acuo.persist.entity.CurrencyEntity;
import com.acuo.persist.entity.FXRate;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.opengamma.strata.basics.currency.Currency;
import org.neo4j.ogm.session.Session;

import javax.inject.Provider;
import java.util.Map;

public class CurrencyServiceImpl extends AbstractService<CurrencyEntity, String> implements CurrencyService {

    private final FXRateService fxRateService;

    @Inject
    public CurrencyServiceImpl(Provider<Session> session, FXRateService fxRateService) {
        super(session);
        this.fxRateService = fxRateService;
    }

    @Override
    public Class<CurrencyEntity> getEntityType() {
        return CurrencyEntity.class;
    }

    /**
     * @deprecated use {@link FXRateService#getFXValue(Currency)}
     */
    @Transactional
    @Override
    @Deprecated
    public Double getFXValue(Currency currency) {
        return fxRateService.getFXValue(currency);
    }

    /**
     * @deprecated use {@link FXRateService#getAllFX()}
     */
    @Transactional
    @Override
    @Deprecated
    public Map<Currency, FXRate> getAllFX() {
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
