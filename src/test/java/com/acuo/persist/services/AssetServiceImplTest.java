package com.acuo.persist.services;

import com.acuo.common.ids.AssetId;
import com.acuo.common.ids.ClientId;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.DataImporter;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.ImportTestServiceModule;
import com.acuo.persist.modules.InProcessNeo4jServerModule;
import com.acuo.persist.utils.ValuationHelper;
import com.opengamma.strata.basics.currency.Currency;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        InProcessNeo4jServerModule.class,
        ImportTestServiceModule.class
})
@Ignore
public class AssetServiceImplTest {

    @Inject
    private DataImporter importService = null;

    @Inject
    private AssetService assetService = null;

    @Inject
    private ValuationHelper valuationHelper = null;

    @Inject
    private AssetTransferService assetTransferService = null;

    private ClientId client999 = ClientId.fromString("999");

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        importService.reload();

        valuationHelper.createAssetValue(Currency.USD);
        valuationHelper.createAssetValue(Currency.EUR);

        assetTransferService.sendAsset("mcp1", AssetId.fromString(Currency.USD.getCode()), 10d, "CustodianAccount1D");
        assetTransferService.receiveAsset("mcp1", AssetId.fromString(Currency.EUR.getCode()), 10d, "CustodianAccount1A");
    }

    @Test
    public void findEligibleAssetByClientId() {
        final Iterable<Asset> eligible = assetService.findAvailableAssetByClientId(client999);
        assertThat(eligible).isNotNull().hasSize(2);
    }

    @Test
    public void findAvailableAssetByClientIdAndCallId() {
        final Iterable<Asset> available = assetService.findAvailableAssetByClientIdAndCallId(client999, "mcp1");
        assertThat(available).isNotNull();
    }

    @Test
    public void findAsset() {
        final Asset usd = assetService.find(AssetId.fromString("USD"));
        assertThat(usd).isNotNull();
        //assertThat(usd.getRules()).hasSize(44);
    }
}