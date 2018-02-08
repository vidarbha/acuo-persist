package com.acuo.persist.entity.trades.fx;

import com.acuo.common.model.BusinessDayAdjustment;
import com.acuo.persist.entity.Entity;
import com.acuo.persist.neo4j.converters.BusinessDayConventionConverter;
import com.acuo.persist.neo4j.converters.CurrencyAmountConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.opengamma.strata.basics.currency.CurrencyAmount;
import com.opengamma.strata.basics.date.BusinessDayConvention;
import com.opengamma.strata.basics.date.HolidayCalendarId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class FxSingle extends Entity<FxSingle> {

    public FxSingle() {}

    //@Relationship(type = "PAYMENT_DATE")
    //private AdjustableDate paymentDate;

    @Convert(LocalDateConverter.class)
    private LocalDate payDate;

    @Convert(BusinessDayConventionConverter.class)
    private BusinessDayConvention payConvention;

    @Convert(CurrencyAmountConverter.class)
    private CurrencyAmount baseCurrencyAmount;

    @Convert(CurrencyAmountConverter.class)
    private CurrencyAmount counterCurrencyAmount;

    private Set<String> payHolidays;

    //@Relationship(type = "FIXING_DATE")
    //private AdjustableDate fixingDate;

    @Convert(LocalDateConverter.class)
    private LocalDate fixingDate;

    @Convert(BusinessDayConventionConverter.class)
    private BusinessDayConvention fixingConvention;

    private Set<String> fixingHolidays;

    private double rate;

    public FxSingle(com.acuo.common.model.product.fx.FxSingle leg) {
        setBaseCurrencyAmount(leg.getBaseCurrencyAmount());
        setCounterCurrencyAmount(leg.getCounterCurrencyAmount());
        //setPaymentDate(new AdjustableDate(leg.getPaymentDate()));
        setPayDate(leg.getPaymentDate().getDate());
        setPayConvention(leg.getPaymentDate().getAdjustment().getBusinessDayConvention());
        setPayHolidays(leg.getPaymentDate().getAdjustment()
                .getHolidays()
                .stream()
                .map(HolidayCalendarId::toString)
                .collect(toSet()));
        //setFixingDate(new AdjustableDate(leg.getFixingDate()));
        setFixingDate(leg.getMaturityDate().getDate());
        setFixingConvention(leg.getMaturityDate().getAdjustment().getBusinessDayConvention());
        setFixingHolidays(leg.getMaturityDate().getAdjustment()
                .getHolidays()
                .stream()
                .map(HolidayCalendarId::toString)
                .collect(toSet()));
        setNonDeliverable(leg.isNonDeliverable());
        setRate(leg.getRate());
    }

    public com.acuo.common.model.product.fx.FxSingle model() {
        com.acuo.common.model.product.fx.FxSingle model = new com.acuo.common.model.product.fx.FxSingle();
        model.setBaseCurrencyAmount(baseCurrencyAmount);
        model.setCounterCurrencyAmount(counterCurrencyAmount);
        //model.setPaymentDate(paymentDate.model());
        com.acuo.common.model.AdjustableDate paymentDate = new com.acuo.common.model.AdjustableDate();
        paymentDate.setDate(payDate);
        BusinessDayAdjustment payAdjustment = new BusinessDayAdjustment();
        payAdjustment.setBusinessDayConvention(payConvention);
        payAdjustment.setHolidays(payHolidays.stream().map(HolidayCalendarId::of).collect(toSet()));
        paymentDate.setAdjustment(payAdjustment);
        model.setPaymentDate(paymentDate);
        //model.setFixingDate(maturityDate.model());
        com.acuo.common.model.AdjustableDate maturityDate = new com.acuo.common.model.AdjustableDate();
        maturityDate.setDate(this.fixingDate);
        BusinessDayAdjustment fixingAdjustment = new BusinessDayAdjustment();
        fixingAdjustment.setBusinessDayConvention(fixingConvention);
        fixingAdjustment.setHolidays(fixingHolidays.stream().map(HolidayCalendarId::of).collect(toSet()));
        maturityDate.setAdjustment(payAdjustment);
        model.setMaturityDate(maturityDate);
        model.setNonDeliverable(nonDeliverable);
        model.setRate(rate);
        return model;
    }

    private boolean nonDeliverable;
}
