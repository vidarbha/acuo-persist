package com.acuo.persist.services;

import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.entity.CurrencyEntity;
import com.acuo.persist.entity.FXRate;
import com.acuo.persist.entity.FXValue;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.DataImporterModule;
import com.acuo.persist.modules.DataLoaderModule;
import com.acuo.persist.modules.Neo4jPersistModule;
import com.acuo.persist.modules.RepositoryModule;
import com.opengamma.strata.basics.currency.Currency;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.time.LocalDateTime;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        Neo4jPersistModule.class,
        DataLoaderModule.class,
        DataImporterModule.class,
        RepositoryModule.class})
public class FXRateServiceImplTest {

    @Inject
    CurrencyService currencyService;

    @Inject
    FXRateService fxRateService;

    private CurrencyEntity usd;
    private CurrencyEntity eur;

    @Before
    public void setUp() {
        usd = createCurrency("USD");
        eur = createCurrency("EUR");
    }

    private CurrencyEntity createCurrency(String id) {
        CurrencyEntity currency = new CurrencyEntity();
        currency.setCurrencyId(id);
        currency.setName(id);
        return currencyService.save(currency);
    }

    @Test
    public void testSaveFxRate() {

        FXRate fxRate = fxRateService.getOrCreate(Currency.USD, Currency.EUR);

        fxRateService.addValue(fxRate, 1d, LocalDateTime.now());
        fxRateService.addValue(fxRate, 2d, LocalDateTime.now());
    }

}