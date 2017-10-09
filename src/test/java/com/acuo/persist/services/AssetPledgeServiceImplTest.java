package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.ImportService;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.AssetPledge;
import com.acuo.persist.entity.AssetTransfer;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.enums.AssetTransferStatus;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.ImportServiceModule;
import com.acuo.persist.modules.RepositoryModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;

import static com.acuo.common.model.margin.Types.AssetType.Cash;
import static com.acuo.common.model.margin.Types.BalanceStatus.Pending;
import static com.acuo.common.model.margin.Types.BalanceStatus.Settled;
import static com.acuo.common.model.margin.Types.MarginType.Variation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        ImportServiceModule.class,
        RepositoryModule.class})
public class AssetPledgeServiceImplTest {

    @Inject
    private ImportService importService = null;

    @Inject
    private AssetPledgeService assetPledgeService = null;

    @Mock
    private AssetTransfer transfer;

    @Mock
    private MarginCall marginCall;

    @Mock
    private Asset asset;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        importService.reload();
    }

    @Test
    public void getFor() throws Exception {
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
    public void getOrCreateFor() throws Exception {
        AssetId assetId = AssetId.fromString("USD");
        AssetPledge b1 = assetPledgeService.getOrCreateFor(assetId, Variation, Pending);
        assertThat(b1).isNotNull();
        assertThat(b1.getMarginType()).isEqualTo(Variation);
        assertThat(b1.getStatus()).isEqualTo(Pending);
    }

    @Test
    public void handle() throws Exception {
        AssetPledge assetPledge = createPledge();

        assertThat(assetPledge).isNotNull();
        assertThat(assetPledge.getStatus()).isEqualTo(Pending);
        assertThat(assetPledge.getMarginType()).isEqualTo(Variation);
        assertThat(assetPledge.getAsset()).isNotNull();
        assertThat(assetPledge.getLatestValue()).isNotNull();
        assertThat(assetPledge.getLatestValue().getAmount()).isEqualTo(20000d);
        assertThat(assetPledge.getValues()).isNotEmpty().hasSize(1);
    }

    @Test
    public void amount() {

        Double amount = assetPledgeService.amount(Cash, Variation, Pending);
        assertThat(amount).isEqualTo(0.0d);

        createPledge();

        amount = assetPledgeService.amount(Cash, Variation, Pending);
        assertThat(amount).isEqualTo(20000.0d);
    }

    private AssetPledge createPledge() {
        when(transfer.getGeneratedBy()).thenReturn(marginCall);
        when(marginCall.getMarginType()).thenReturn(Variation);

        when(transfer.getOf()).thenReturn(asset);
        when(asset.getAssetId()).thenReturn(AssetId.fromString("USD"));

        when(transfer.getStatus()).thenReturn(AssetTransferStatus.Departed);

        when(transfer.getQuantities()).thenReturn(10000d);
        when(transfer.getTransferValue()).thenReturn(2d);

       return assetPledgeService.handle(transfer);
    }
}