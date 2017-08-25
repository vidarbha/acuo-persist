package com.acuo.persist.entity;

import com.acuo.persist.entity.enums.Side;
import com.acuo.persist.entity.enums.StatementDirection;
import com.google.common.collect.ImmutableMap;
import com.opengamma.strata.basics.currency.Currency;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;

import static com.acuo.persist.entity.enums.StatementDirection.IN;
import static com.acuo.persist.entity.enums.StatementDirection.OUT;
import static com.opengamma.strata.basics.currency.Currency.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class VariationMarginTest {

    private final Currency currency = USD;
    private final LocalTime notificationTime = LocalTime.now();
    private final LocalDate now = LocalDate.now();
    private final long tradeCount = 1L;

    private final Double balance;
    private final Double pending;
    private final Double amount;
    private final StatementDirection direction;
    private final Double exposure;
    private final Double _return;
    private final Double excess;
    private final Double deliver;
    private final Double margin;

    @Mock
    private Agreement agreement;

    @Mock
    private MarginStatement statement;

    @Mock
    private ClientSignsRelation clientSignsRelation;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
        //Amount    //Balance   //Pending   //Excess    //Direction     //Exposure  //Deliver   //Return    //Margin
        { 200.0d,   0.0d,       0.0d,       200.0d,     IN,             200.0d,     200.0d,     0.0d,       200.0d  },
        { 100.0d,   200.0d,     0.0d,       -100.0d,    OUT,            100.0d,     0.0d,       100.0d,     100.0d  },
        { 150.0d,   100.0d,     0.0d,       50.0d,      IN,             150.0d,     50.0d,      0.0d,       50.0d   },
        { -50.0d,   150.0d,     0.0d,       -200.0d,    OUT,            -50.0d,     50.0d,      150.0d,     200.0d  },
        { -150.0d,  -50.0d,     0.0d,       -100.0d,    OUT,            -150.0d,    100.0d,     0.0d,       100.0d  },
        { -50.0d,   -150.0d,    0.0d,       100.0d,     IN,             -50.0d,     0.0d,       100.0d,     100.0d  },
        { 100.0d,   -50.0d,     0.0d,       150.0d,     IN,             100.0d,     100.0d,     50.0d,      150.0d  }
    });
    }

    public VariationMarginTest(Double amount,
                               Double balance,
                               Double pending,
                               Double excess,
                               StatementDirection direction,
                               Double exposure,
                               Double deliver,
                               Double _return,
                               Double margin
    ) {
        this.amount = amount;
        this.balance = balance;
        this.pending = pending;
        this.excess = excess;
        this.direction = direction;
        this.exposure = exposure;
        this.deliver = deliver;
        this._return = _return;
        this.margin = margin;
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test() {
        when(agreement.getCurrency()).thenReturn(currency);
        when(agreement.getNotificationTime()).thenReturn(notificationTime);
        when(agreement.getClientSignsRelation()).thenReturn(clientSignsRelation);
        when(statement.variationBalance()).thenReturn(balance);
        when(statement.variationPending()).thenReturn(pending);

        final ImmutableMap<Currency, Double> rates = ImmutableMap.of(currency, 1d);

        final VariationMargin variation = new VariationMargin(Side.Client,
                amount,
                now,
                now,
                currency,
                agreement,
                statement,
                rates,
                tradeCount);

        assertThat(variation).isNotNull();
        assertThat(variation.getExposure()).isEqualTo(exposure);
        assertThat(variation.getReturnAmount()).isEqualTo(_return);
        assertThat(variation.getExcessAmount()).isEqualTo(excess);
        assertThat(variation.getDeliverAmount()).isEqualTo(deliver);
        assertThat(variation.getMarginAmount()).isEqualTo(margin);
        assertThat(variation.getDirection()).isEqualTo(direction);
    }

}