package com.acuo.persist.services;

import com.acuo.common.ids.TradeId;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.ImportService;
import com.acuo.persist.entity.trades.FRA;
import com.acuo.persist.entity.trades.Leg;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.ImportServiceModule;
import com.acuo.persist.modules.InProcessNeo4jServerModule;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        InProcessNeo4jServerModule.class,
        ImportServiceModule.class
})
public class TradeServiceImplTest {

    @Inject
    private ImportService importService = null;

    @Inject
    private TradeService<FRA> fraTradeService = null;

    @Before
    public void setUp() throws Exception {
        importService.reload();
    }

    @Test
    public void testCreateAndUpdateTradeAndAvoidCreatingMultipleLegsOW754() throws Exception {

        fraTradeService.createOrUpdate(ImmutableSet.of(createFRA()));
        fraTradeService.createOrUpdate(ImmutableSet.of(createFRA()));

        FRA fra = fraTradeService.find(TradeId.fromString("fra"));
        assertThat(fra.getPayLegs()).hasSize(1);
        assertThat(fra.getReceiveLegs()).hasSize(1);
    }

    private FRA createFRA() {
        FRA fra = new FRA();
        fra.setTradeId(TradeId.fromString("fra"));
        Leg payLeg = new Leg();
        payLeg.setLegId("1");
        Leg receiveLeg = new Leg();
        receiveLeg.setLegId("2");
        fra.setPayLegs(ImmutableSet.of(payLeg));
        fra.setReceiveLegs(ImmutableSet.of(receiveLeg));
        return fra;
    }

}