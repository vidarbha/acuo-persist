package com.acuo.persist.services;

import com.acuo.common.ids.AssetId;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.DataImporter;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.AssetPledge;
import com.acuo.persist.entity.AssetTransfer;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.enums.AssetTransferStatus;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.ImportTestServiceModule;
import com.acuo.persist.modules.InProcessNeo4jServerModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;
import java.util.Optional;

import static com.acuo.common.model.margin.Types.AssetType.Cash;
import static com.acuo.common.model.margin.Types.BalanceStatus.Pending;
import static com.acuo.common.model.margin.Types.BalanceStatus.Settled;
import static com.acuo.common.model.margin.Types.MarginType.Variation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        InProcessNeo4jServerModule.class,
        ImportTestServiceModule.class
})
public class AssetPledgeServiceImplTest {

    @Inject
    private DataImporter importService = null;

    @Inject
    private AssetPledgeService assetPledgeService = null;

    @Mock
    private AssetTransfer transfer;

    @Mock
    private MarginCall marginCall;

    @Mock
    private Asset asset;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
    public void getFor() {
        AssetId assetId = AssetId.fromString("USD");
        AssetPledge b1 = assetPledgeService.getFor(assetId, Variation, Settled);
        assertThat(b1).isNull();
        assetPledgeService.getOrCreateFor(assetId, Variation, Settled);
        b1 = assetPledgeService.getFor(assetId, Variation, Settled);
        assertThat(b1).isNotNull();
        assertThat(b1.getMarginType()).isEqualTo(Variation);
        assertThat(b1.getStatus()).isEqualTo(Settled);
    }

    @Test
    public void getOrCreateFor() {
        AssetId assetId = AssetId.fromString("USD");
        AssetPledge b1 = assetPledgeService.getOrCreateFor(assetId, Variation, Pending);
        assertThat(b1).isNotNull();
        assertThat(b1.getMarginType()).isEqualTo(Variation);
        assertThat(b1.getStatus()).isEqualTo(Pending);
    }

    @Test
    public void handle() {
        Optional<AssetPledge> assetPledge = createPledge();

        assertThat(assetPledge).isNotNull();
        assertThat(assetPledge).hasValueSatisfying(value -> {
            assertThat(value.getStatus()).isEqualTo(Pending);
            assertThat(value.getMarginType()).isEqualTo(Variation);
            assertThat(value.getAsset()).isNotNull();
            assertThat(value.getLatestValue()).isNotNull();
            assertThat(value.getLatestValue().getAmount()).isEqualTo(20000d);
            assertThat(value.getValues()).isNotEmpty().hasSize(1);
        });
    }

    @Test
    public void amount() {

        Double amount = assetPledgeService.amount(Cash, Variation, Pending);
        assertThat(amount).isEqualTo(0.0d);

        createPledge();

        amount = assetPledgeService.amount(Cash, Variation, Pending);
        assertThat(amount).isEqualTo(20000.0d);
    }

    private Optional<AssetPledge> createPledge() {

        when(transfer.getGeneratedBy()).thenReturn(marginCall);
        when(marginCall.getMarginType()).thenReturn(Variation);

        when(transfer.getOf()).thenReturn(asset);
        when(asset.getAssetId()).thenReturn(AssetId.fromString("USD"));

        when(transfer.getStatus()).thenReturn(AssetTransferStatus.Departed);

        when(transfer.getQuantity()).thenReturn(10000d);
        when(transfer.getUnitValue()).thenReturn(2d);

       return assetPledgeService.handle(transfer);
    }
}