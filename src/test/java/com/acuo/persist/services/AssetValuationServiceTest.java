package com.acuo.persist.services;

import com.acuo.common.ids.AssetId;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.DataImporter;
import com.acuo.persist.entity.AssetValuation;
import com.acuo.persist.entity.AssetValue;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.ImportTestServiceModule;
import com.acuo.persist.modules.InProcessNeo4jServerModule;
import com.opengamma.strata.basics.currency.Currency;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        InProcessNeo4jServerModule.class,
        ImportTestServiceModule.class
})
public class AssetValuationServiceTest {

    @Inject
    private DataImporter importService = null;

    @Inject
    private AssetValuationService assetValuationService = null;

    @Before
    public void setUp() {
        importService.deleteAll();
        importService.load("ACUO",
                "firms","counterparts",
                "workingZones","legalentities",
                "clearingHouses","fcms",
                "tradingAccounts","bilateralMasterAgreements",
                "bilateralAgreements","clearedAgreements",
                "ratingScores","assetCategories",
                "custodianAccounts","counterpartCustodianAccounts",
                "custodianAssets","buildEligibility",
                "settings","currencies",
                "fxRates","portfolios","books");
    }

    @Test
    public void testAssetValuationService() {

        final AssetId usd = AssetId.fromString("USD");
        AssetValue newValue = createAssetValue(Currency.USD, 1.0d, "Reuters");

        assetValuationService.persist(usd, newValue);

        newValue = createAssetValue(Currency.USD, 1.1d, "Reuters");
        assetValuationService.persist(usd, newValue);

        AssetValuation valuation = assetValuationService.getAssetValuationFor(usd);

        final AssetValue latestValue = valuation.getLatestValue();
        assertThat(latestValue).isNotNull();
    }

    private AssetValue createAssetValue(Currency currency, Double amount, String source) {
        AssetValue newValue = new AssetValue();
        newValue.setSource(source);
        newValue.setCoupon(amount);
        newValue.setNominalCurrency(currency);
        newValue.setReportCurrency(currency);
        newValue.setTimestamp(Instant.now());
        newValue.setValuationDate(LocalDate.now());
        return newValue;
    }
}
