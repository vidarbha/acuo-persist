package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.ImportService;
import com.acuo.persist.entity.Settlement;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.ImportServiceModule;
import com.acuo.persist.modules.RepositoryModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        ImportServiceModule.class,
        RepositoryModule.class})
public class SettlementServiceImplTest {

    @Inject
    private ImportService importService = null;

    @Inject
    private SettlementService service = null;

    @Before
    public void setUp() throws Exception {
        importService.reload();
    }

    @Test
    public void testGetOrCreateSettlementDateFor() throws Exception {
        AssetId assetId = AssetId.fromString("SG76D1000009");
        Settlement settlement = service.getSettlementFor(assetId);
        assertThat(settlement).isNull();
        settlement = service.getOrCreateSettlementFor(assetId);
        assertThat(settlement).isNotNull();
    }
}