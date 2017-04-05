package com.acuo.persist.services;

import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.core.ImportService;
import com.acuo.persist.entity.TradeValuation;
import com.acuo.persist.entity.TradeValue;
import com.acuo.persist.entity.TradeValueRelation;
import com.acuo.persist.entity.ValueRelation;
import com.acuo.persist.ids.PortfolioId;
import com.acuo.persist.modules.*;
import com.opengamma.strata.basics.currency.Currency;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.time.LocalDate;
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
    ImportService importService;

    @Inject
    ValuationService valuationService;

    @Inject
    ValueService valueService;

    @Before
    public void setUp() {
        importService.reload();
    }

    @Test
    public void testValuationService() {

        TradeValuation valuation = valuationService.getOrCreateTradeValuationFor(PortfolioId.fromString("p2"));

        TradeValue newValue = createValue(Currency.USD, 1.0d, "Markit");
        TradeValueRelation valueRelation = new TradeValueRelation();
        valueRelation.setValuation(valuation);
        valueRelation.setDateTime(LocalDate.now());
        valueRelation.setValue(newValue);
        newValue.setValuation(valueRelation);

        TradeValue value = valueService.save(newValue);

        valuation = valuationService.getTradeValuationFor(PortfolioId.fromString("p2"));

        Set<TradeValueRelation> values = valuation.getValues();
        assertThat(values).isNotEmpty();
    }

    private TradeValue createValue(Currency currency, Double pv, String source) {
        TradeValue newValue = new TradeValue();
        newValue.setSource(source);
        newValue.setCurrency(currency);
        newValue.setPv(pv);
        return newValue;
    }
}
