package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.*;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.date.BusinessDayConvention;
import com.opengamma.strata.basics.date.DayCount;
import com.opengamma.strata.basics.date.Tenor;
import com.opengamma.strata.basics.index.FloatingRateName;
import com.opengamma.strata.basics.schedule.Frequency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Leg extends Entity<Leg> {

    private Double notional;

    @Convert(DayCountConverter.class)
    private DayCount dayCount;

    @Convert(BusinessDayConventionConverter.class)
    private BusinessDayConvention businessDayConvention;

    private Double fixedRate;

    @Convert(LocalDateConverter.class)
    private LocalDate payStart;

    @Property(name="id")
    private String legId;

    private String legNumber;

    @Convert(FrequencyConverter.class)
    private Frequency paymentFrequency;

    private String type;

    @Convert(LocalDateConverter.class)
    private LocalDate nextCouponPaymentDate;

    @Convert(LocalDateConverter.class)
    private LocalDate payEnd;

    private String refCalendar;

    @Convert(TenorConverter.class)
    private Tenor indexTenor;

    @Convert(FloatingRateNameConverter.class)
    private FloatingRateName index;

    @Convert(FrequencyConverter.class)
    private Frequency resetFrequency;

    @Convert(CurrencyConverter.class)
    private Currency currency;

}
