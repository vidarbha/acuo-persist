package com.acuo.persist.services;

import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.ImportService;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.ids.ClientId;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.DataImporterModule;
import com.acuo.persist.modules.DataLoaderModule;
import com.acuo.persist.modules.Neo4jPersistModule;
import com.acuo.persist.modules.RepositoryModule;
import com.acuo.persist.utils.ValuationHelper;
import com.opengamma.strata.basics.currency.Currency;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        Neo4jPersistModule.class,
        DataLoaderModule.class,
        DataImporterModule.class,
        RepositoryModule.class})
public class AssetServiceImplTest {

    @Inject
    private ImportService importService = null;

    @Inject
    private AssetService assetService = null;

    @Inject
    private ValuationHelper valuationHelper = null;

    @Inject
    private AssetTransferService assetTransferService = null;

    private ClientId client999 = ClientId.fromString("999");

    @Before
    public void setUp() throws Exception {
        importService.reload();

        valuationHelper.createAssetValue(Currency.USD);
        valuationHelper.createAssetValue(Currency.EUR);

        assetTransferService.sendAsset("mcp1", Currency.USD.getCode(), 10d, "CustodianAccount1D");
        assetTransferService.receiveAsset("mcp1", Currency.EUR.getCode(), 10d, "CustodianAccount1A");
    }

    @Test
    public void findEligibleAssetByClientId() throws Exception {
        final Iterable<Asset> eligible = assetService.findAvailableAssetByClientId(client999);
        assertThat(eligible).isNotNull().hasSize(0);
    }

    @Test
    public void findAvailableAssetByClientIdAndCallId() throws Exception {
        final Iterable<Asset> available = assetService.findAvailableAssetByClientIdAndCallId(client999, "mcp1");
        assertThat(available).isNotNull();
    }

    @Test
    public void findAsset() {
        final Asset usd = assetService.find("USD");
        assertThat(usd).isNotNull();
        assertThat(usd.getRules()).hasSize(44);
    }
}