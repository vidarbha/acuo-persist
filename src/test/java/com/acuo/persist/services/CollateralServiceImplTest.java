package com.acuo.persist.services;

import com.acuo.common.model.margin.Types;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.ImportService;
import com.acuo.persist.entity.Agreement;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.AssetTransfer;
import com.acuo.persist.entity.Collateral;
import com.acuo.persist.entity.CollateralValue;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.MarginStatement;
import com.acuo.persist.entity.enums.AssetTransferStatus;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.DataImporterModule;
import com.acuo.persist.modules.DataLoaderModule;
import com.acuo.persist.modules.Neo4jPersistModule;
import com.acuo.persist.modules.RepositoryModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;

import java.util.Set;

import static com.acuo.common.model.margin.Types.AssetType.Cash;
import static com.acuo.common.model.margin.Types.BalanceStatus.Pending;
import static com.acuo.common.model.margin.Types.BalanceStatus.Settled;
import static com.acuo.common.model.margin.Types.MarginType.Variation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        Neo4jPersistModule.class,
        DataLoaderModule.class,
        DataImporterModule.class,
        RepositoryModule.class})
public class CollateralServiceImplTest {

    @Inject
    private ImportService importService = null;

    @Inject
    private CollateralService collateralService = null;

    @Inject
    private CollateralValueService collateralValueService = null;

    @Mock
    private AssetTransfer transfer;

    @Mock
    private MarginCall marginCall;

    @Mock
    private MarginStatement marginStatement;

    @Mock
    private Agreement agreement;

    @Mock
    private Asset asset;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        importService.reload();
    }

    @Test
    public void getCollateralFor() throws Exception {
        Collateral b1 = collateralService.getCollateralFor("a1", Variation, Cash, Settled);
        assertThat(b1).isNull();
        collateralService.getOrCreateCollateralFor("a1", Variation, Cash, Settled);
        b1 = collateralService.getCollateralFor("a1", Variation, Cash, Settled);
        assertThat(b1).isNotNull();
        assertThat(b1.getMarginType()).isEqualTo(Variation);
        assertThat(b1.getAssetType()).isEqualTo(Cash);
        assertThat(b1.getStatus()).isEqualTo(Settled);
    }

    @Test
    public void getOrCreateCollateralFor() throws Exception {
        Collateral b1 = collateralService.getOrCreateCollateralFor("a1", Variation, Cash, Pending);
        assertThat(b1).isNotNull();
        assertThat(b1.getMarginType()).isEqualTo(Variation);
        assertThat(b1.getAssetType()).isEqualTo(Cash);
        assertThat(b1.getStatus()).isEqualTo(Pending);
    }

    @Test
    public void testHandle() {

        when(transfer.getGeneratedBy()).thenReturn(marginCall);
        when(marginCall.getMarginType()).thenReturn(Types.MarginType.Variation);
        when(marginCall.getMarginStatement()).thenReturn(marginStatement);
        when(marginStatement.getAgreement()).thenReturn(agreement);
        when(agreement.getAgreementId()).thenReturn("a1");

        when(transfer.getOf()).thenReturn(asset);
        when(asset.getType()).thenReturn("CASH");

        when(transfer.getStatus()).thenReturn(AssetTransferStatus.Departed);

        when(transfer.getQuantities()).thenReturn(10000d);
        when(transfer.getTransferValue()).thenReturn(2d);

        Collateral collateral = collateralService.handle(transfer);

        assertThat(collateral).isNotNull();
        assertThat(collateral.getStatus()).isEqualTo(Types.BalanceStatus.Pending);
        assertThat(collateral.getAssetType()).isEqualTo(Types.AssetType.Cash);
        assertThat(collateral.getMarginType()).isEqualTo(Types.MarginType.Variation);
        assertThat(collateral.getAgreement()).isNotNull();
        assertThat(collateral.getLatestValue()).isNotNull();
        assertThat(collateral.getLatestValue().getAmount()).isEqualTo(20000d);
        assertThat(collateral.getValues()).isNotEmpty().hasSize(1);
    }

    @Test
    public void testSaveValues() {
        Collateral collateral = collateralService.getOrCreateCollateralFor("a1", Variation, Cash, Pending);

        CollateralValue collateralValue = collateralValueService.createCollateralValue(10d);
        collateralValue.setCollateral(collateral);

        collateralValue = collateralValueService.save(collateralValue);

        assertThat(collateralValue).isNotNull();

        collateral = collateralService.getCollateralFor("a1", Variation, Cash, Pending);

        Set<CollateralValue> values = collateral.getValues();

        assertThat(values).isNotEmpty();

    }

}