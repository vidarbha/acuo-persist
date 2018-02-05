package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.TradeId;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.ImportService;
import com.acuo.persist.entity.trades.FRA;
import com.acuo.persist.entity.trades.Leg;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.ImportTestServiceModule;
import com.acuo.persist.modules.InProcessNeo4jServerModule;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        InProcessNeo4jServerModule.class,
        ImportTestServiceModule.class,
})
public class TradeServiceImplTest {

    @Inject
    private ImportService importService = null;

    @Inject
    private TradingAccountService accountService = null;

    @Inject
    private TradeService<FRA> fraTradeService = null;

    private ClientId client999 = ClientId.fromString("999");

    @Before
    public void setUp() {
        importService.reload();
    }

    @Test
    public void testCreateAndUpdateTradeAndAvoidCreatingMultipleLegsOW754() {

        fraTradeService.createOrUpdate(client999, ImmutableSet.of(createFRA("ACUOSG8745")));
        fraTradeService.createOrUpdate(client999, ImmutableSet.of(createFRA("ACUOSG8745")));
        fraTradeService.createOrUpdate(client999, ImmutableSet.of(createFRA("ACUOSG3126")));

        Iterable<FRA> trades = fraTradeService.findAllClientTrades(client999);
        assert Iterables.size(trades) == 1;
        FRA fra = Iterables.get(trades, 0);
        assertThat(fra.getPayLegs()).hasSize(1);
        assertThat(fra.getReceiveLegs()).hasSize(1);
    }

    private FRA createFRA(String account) {
        FRA fra = new FRA();
        fra.setTradeId(TradeId.fromString("fra"));
        fra.setAcct(account);
        Leg payLeg = new Leg();
        payLeg.setLegId("1");
        Leg receiveLeg = new Leg();
        receiveLeg.setLegId("2");
        fra.setPayLegs(ImmutableSet.of(payLeg));
        fra.setReceiveLegs(ImmutableSet.of(receiveLeg));
        fra.setAccount(accountService.account(client999, account));
        return fra;
    }

}