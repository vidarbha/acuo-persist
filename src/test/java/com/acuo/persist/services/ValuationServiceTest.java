package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioName;
import com.acuo.common.ids.TradeId;
import com.acuo.common.model.margin.Types;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.DataImporter;
import com.acuo.persist.entity.MarginValuation;
import com.acuo.persist.entity.MarginValue;
import com.acuo.persist.entity.TradeValuation;
import com.acuo.persist.entity.TradeValue;
import com.acuo.persist.entity.trades.IRS;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.ImportTestServiceModule;
import com.acuo.persist.modules.InProcessNeo4jServerModule;
import com.opengamma.strata.basics.currency.Currency;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        InProcessNeo4jServerModule.class,
        ImportTestServiceModule.class
})
public class ValuationServiceTest {

    @Inject
    private DataImporter importService = null;

    @Inject
    private ValuationService valuationService = null;

    @Inject
    private ValueService valueService = null;

    @Inject
    private TradeService<IRS> tradeService = null;

    @Inject
    private TradingAccountService accountService = null;

    private ClientId client999 = ClientId.fromString("999");

    @Before
    public void setUp() {
        importService.reload();
    }

    @Test
    public void testMarginValuationService() {

        MarginValuation valuation = valuationService.getOrCreateMarginValuationFor(client999, PortfolioName.fromString("p2"), Types.CallType.Variation);

        MarginValue newValue = createMarginValue(Currency.USD, 1.0d, "Markit");
        newValue.setValuation(valuation);

        MarginValue value = valueService.save(newValue);

        assertThat(value).isNotNull();

        valuation = valuationService.getMarginValuationFor(client999, PortfolioName.fromString("p2"), Types.CallType.Variation);

        Set<MarginValue> values = valuation.getValues();
        assertThat(values).isNotEmpty();
    }

    @Test
    public void testTradeValuationService() {

        TradeId t1 = TradeId.fromString("t1");
        IRS entity = new IRS();
        entity.setTradeId(t1);
        entity.setAccount(accountService.account(client999, "ACUOSG8745"));
        tradeService.save(entity);
        TradeValuation valuation = valuationService.getOrCreateTradeValuationFor(client999, t1);

        TradeValue newValue = createTradeValue(Currency.USD, 1.0d, "DataScope");
        newValue.setValuation(valuation);

        TradeValue value = valueService.save(newValue);

        assertThat(value).isNotNull();

        valuation = valuationService.getTradeValuationFor(client999, t1);

        Set<TradeValue> values = valuation.getValues();
        assertThat(values).isNotEmpty();
    }

    private MarginValue createMarginValue(Currency currency, Double amount, String source) {
        MarginValue newValue = new MarginValue();
        newValue.setSource(source);
        newValue.setCurrency(currency);
        newValue.setAmount(amount);
        newValue.setTimestamp(Instant.now());
        return newValue;
    }

    private TradeValue createTradeValue(Currency currency, Double amount, String source) {
        TradeValue newValue = new TradeValue();
        newValue.setSource(source);
        newValue.setPv(amount);
        newValue.setCurrency(currency);
        newValue.setTimestamp(Instant.now());
        return newValue;
    }
}
