package com.acuo.persist.services;

import com.acuo.common.model.margin.Types;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.ImportService;
import com.acuo.persist.entity.AssetValuation;
import com.acuo.persist.entity.AssetValue;
import com.acuo.persist.entity.IRS;
import com.acuo.persist.entity.MarginValuation;
import com.acuo.persist.entity.MarginValue;
import com.acuo.persist.entity.TradeValuation;
import com.acuo.persist.entity.TradeValue;
import com.acuo.common.model.ids.AssetId;
import com.acuo.common.model.ids.PortfolioId;
import com.acuo.common.model.ids.TradeId;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.DataImporterModule;
import com.acuo.persist.modules.DataLoaderModule;
import com.acuo.persist.modules.Neo4jPersistModule;
import com.acuo.persist.modules.RepositoryModule;
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
        Neo4jPersistModule.class,
        DataLoaderModule.class,
        DataImporterModule.class,
        RepositoryModule.class})
public class ValuationServiceTest {

    @Inject
    private ImportService importService = null;

    @Inject
    private ValuationService valuationService = null;

    @Inject
    private ValueService valueService = null;

    @Inject
    private TradeService<IRS> tradeService = null;

    @Before
    public void setUp() {
        importService.reload();
    }

    @Test
    public void testMarginValuationService() {

        MarginValuation valuation = valuationService.getOrCreateMarginValuationFor(PortfolioId.fromString("p2"), Types.CallType.Variation);

        MarginValue newValue = createMarginValue(Currency.USD, 1.0d, "Markit");
        newValue.setValuation(valuation);

        MarginValue value = valueService.save(newValue);

        assertThat(value).isNotNull();

        valuation = valuationService.getMarginValuationFor(PortfolioId.fromString("p2"), Types.CallType.Variation);

        Set<MarginValue> values = valuation.getValues();
        assertThat(values).isNotEmpty();
    }

    @Test
    public void testAssetValuationService() {

        AssetValuation valuation = valuationService.getOrCreateAssetValuationFor(AssetId.fromString("USD"));

        AssetValue newValue = createAssetValue(Currency.USD, 1.0d, "Reuters");
        newValue.setValuation(valuation);

        AssetValue value = valueService.save(newValue);

        assertThat(value).isNotNull();

        valuation = valuationService.getAssetValuationFor(AssetId.fromString("USD"));

        Set<AssetValue> values = valuation.getValues();
        assertThat(values).isNotEmpty();
    }

    @Test
    public void testTradeValuationService() {

        TradeId t1 = TradeId.fromString("t1");
        IRS entity = new IRS();
        entity.setTradeId(t1);
        tradeService.save(entity);
        TradeValuation valuation = valuationService.getOrCreateTradeValuationFor(t1);

        TradeValue newValue = createTradeValue(Currency.USD, 1.0d, "DataScope");
        newValue.setValuation(valuation);

        TradeValue value = valueService.save(newValue);

        assertThat(value).isNotNull();

        valuation = valuationService.getTradeValuationFor(t1);

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

    private AssetValue createAssetValue(Currency currency, Double amount, String source) {
        AssetValue newValue = new AssetValue();
        newValue.setSource(source);
        newValue.setCoupon(amount);
        newValue.setNominalCurrency(currency);
        newValue.setReportCurrency(currency);
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
