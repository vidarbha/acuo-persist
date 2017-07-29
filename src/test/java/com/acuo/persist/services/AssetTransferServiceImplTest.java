package com.acuo.persist.services;

import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.ImportService;
import com.acuo.persist.entity.AssetTransfer;
import com.acuo.common.model.ids.AssetId;
import com.acuo.common.model.ids.ClientId;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.DataImporterModule;
import com.acuo.persist.modules.DataLoaderModule;
import com.acuo.persist.modules.Neo4jPersistModule;
import com.acuo.persist.modules.RepositoryModule;
import com.acuo.persist.utils.ValuationHelper;
import com.opengamma.strata.basics.currency.Currency;
import org.junit.Before;
import org.junit.Ignore;
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
public class AssetTransferServiceImplTest {

    @Inject
    private ImportService importService = null;

    @Inject
    private ValuationHelper valuationHelper = null;

    @Inject
    private AssetTransferService transferService = null;

    private ClientId client999 = ClientId.fromString("999");

    @Before
    public void setUp() throws Exception {
        importService.reload();

        valuationHelper.createAssetValue(Currency.USD);
    }

    @Test
    public void findArrivingAssetTransferByClientId() throws Exception {
        transferService.receiveAsset("mcp1", AssetId.fromString(Currency.USD.getCode()), 10d, "CustodianAccount1D");

        final Iterable<AssetTransfer> arriving = transferService.findArrivingAssetTransferByClientId(client999);
        assertThat(arriving).isNotNull().hasSize(1);
    }

    @Test
    public void findDepartedAssetTransferByClientId() throws Exception {
        transferService.sendAsset("mcp1", AssetId.fromString(Currency.USD.getCode()), 10d, "CustodianAccount1D");

        final Iterable<AssetTransfer> departed = transferService.findDepartedAssetTransferByClientId(client999);
        assertThat(departed).isNotNull().hasSize(1);
    }

    @Test
    @Ignore
    public void sendAsset() throws Exception {
    }

    @Test
    @Ignore
    public void receiveAsset() throws Exception {
    }

    @Test
    @Ignore
    public void getPledgedAssets() throws Exception {
    }

}