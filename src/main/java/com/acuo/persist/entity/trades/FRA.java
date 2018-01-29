package com.acuo.persist.entity.trades;

import com.acuo.common.model.AdjustableDate;
import com.acuo.common.model.AdjustableSchedule;
import com.acuo.common.model.BusinessDayAdjustment;
import com.acuo.common.model.trade.FRATrade;
import com.acuo.common.model.trade.ProductType;
import com.acuo.common.model.trade.TradeInfo;
import com.acuo.persist.entity.PricingSource;
import com.acuo.persist.entity.enums.PricingProvider;
import com.google.common.collect.ImmutableSet;
import com.opengamma.strata.basics.date.HolidayCalendarId;
import com.opengamma.strata.basics.date.Tenor;
import com.opengamma.strata.basics.schedule.RollConvention;
import com.opengamma.strata.product.common.PayReceive;
import com.opengamma.strata.product.swap.FixingRelativeTo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class FRA extends Trade<FRA> {

    public FRA() {}

    public FRA(FRATrade model) {

        super(model);

        com.acuo.common.model.product.FRA fra = model.getProduct();

        Set<Leg> payLegs = new HashSet<>();
        Set<Leg> receiveLegs = new HashSet<>();

        setPayLegs(payLegs);
        setReceiveLegs(receiveLegs);

        if (TRADE_TYPE_BILATERAL.equalsIgnoreCase(getTradeType())) {
            PricingSource pricingSource = new PricingSource();
            pricingSource.setName(PricingProvider.Markit);
            setPricingSource(pricingSource);
        } else {
            PricingSource pricingSource = new PricingSource();
            pricingSource.setName(PricingProvider.Clarus);
            setPricingSource(pricingSource);
        }

        //leg
        int legId = 0;
        for (com.acuo.common.model.product.FRA.FRALeg fraLeg : fra.getLegs()) {
            Leg leg = new Leg();
            if (PayReceive.PAY.equals(fraLeg.getPayReceive()))
                payLegs.add(leg);
            else
                receiveLegs.add(leg);

            legId++;
            leg.setLegId(getTradeId() + "-" + legId);
            leg.setType(fraLeg.getType());
            leg.setCurrency(fraLeg.getCurrency());
            leg.setPayStart(fraLeg.getStartDate().getDate());
            leg.setPayEnd(fraLeg.getMaturityDate().getDate());
            leg.setDayCount(fraLeg.getDaycount());
            leg.setNotional(fraLeg.getNotional());
            if (fraLeg.getRate() != null)
                leg.setFixedRate(fraLeg.getRate() / 100);
            if (fraLeg.getPaymentDate().getAdjustment() != null) {
                if (fraLeg.getPaymentDate().getAdjustment().getHolidays() != null &&
                        fraLeg.getPaymentDate().getAdjustment().getHolidays().size() > 0) {
                    final String name = fraLeg.getPaymentDate().getAdjustment().getHolidays().iterator().next().getName();
                    leg.setRefCalendar(name);
                }
                leg.setBusinessDayConvention(fraLeg.getPaymentDate().getAdjustment().getBusinessDayConvention());
            }

            if (fraLeg.getFixing() != null) {
                com.acuo.common.model.product.FRA.FRALegFixing fixing = fraLeg.getFixing();
                leg.setIndex(fixing.getFloatingRateName());
                leg.setIndexTenor(fixing.getTenor());
            }
        }
    }

    public FRATrade model() {
        FRATrade fraTrade = new FRATrade();
        com.acuo.common.model.product.FRA fra = new com.acuo.common.model.product.FRA();
        fraTrade.setProduct(fra);
        fraTrade.setType(ProductType.FRA);

        TradeInfo info = info();
        info.setDiscountMethod("ISDA");
        fraTrade.setInfo(info);

        Set<Leg> receiveLegs = getReceiveLegs();
        if(receiveLegs != null)
            for (Leg receiveLeg : receiveLegs) {
                com.acuo.common.model.product.FRA.FRALeg leg = buildFRALeg(1,receiveLeg);
                leg.setPayReceive(PayReceive.RECEIVE);
                leg.setRollConvention(RollConvention.ofDayOfMonth(10));
                fra.addLeg(leg);
            }

        Set<Leg> payLegs = getPayLegs();
        if(payLegs != null)
            for (Leg payLeg : payLegs) {
                com.acuo.common.model.product.FRA.FRALeg leg = buildFRALeg(2, payLeg);
                leg.setPayReceive(PayReceive.PAY);
                leg.setRollConvention(RollConvention.ofDayOfMonth(10));
                leg.setNotional( -1 * leg.getNotional());
                fra.addLeg(leg);
            }
        return fraTrade;
    }

    private com.acuo.common.model.product.FRA.FRALeg buildFRALeg(int id, Leg leg) {
        com.acuo.common.model.product.FRA.FRALeg  result = new com.acuo.common.model.product.FRA.FRALeg ();

        result.setId(id);
        result.setCurrency(leg.getCurrency());
        result.setNotional(leg.getNotional());
        result.setRate(leg.getFixedRate());
        result.setDaycount(leg.getDayCount());
        result.setType(leg.getType());

        com.acuo.common.model.AdjustableDate adjustableDate = new com.acuo.common.model.AdjustableDate();
        adjustableDate.setDate(leg.getPayStart());
        com.acuo.common.model.BusinessDayAdjustment adjustment = new BusinessDayAdjustment();
        adjustment.setBusinessDayConvention(leg.getBusinessDayConvention());

        HolidayCalendarId holidays = holidays(leg);

        adjustment.setHolidays(ImmutableSet.of(holidays));
        adjustableDate.setAdjustment(adjustment);
        result.setStartDate(adjustableDate);

        adjustableDate = new AdjustableDate();
        adjustableDate.setDate(leg.getPayEnd());
        adjustableDate.setAdjustment(adjustment);
        result.setMaturityDate(adjustableDate);
        result.setPaymentDate(adjustableDate);

        AdjustableSchedule adjustableSchedule = new AdjustableSchedule();
        adjustableSchedule.setFrequency(leg.getPaymentFrequency());
        adjustableSchedule.setAdjustment(adjustment);

        if ("FLOAT".equals(leg.getType())) {
            com.acuo.common.model.product.FRA.FRALegFixing swapLegFixing = new com.acuo.common.model.product.FRA.FRALegFixing ();
            result.setFixing(swapLegFixing);

            swapLegFixing.setTenor(leg.getIndexTenor());
            if(swapLegFixing.getTenor() == null)
                swapLegFixing.setTenor(Tenor.TENOR_1D);

            swapLegFixing.setFloatingRateName(leg.getIndex());

            swapLegFixing.setFixingRelativeTo(FixingRelativeTo.PERIOD_START);
        }
        return result;
    }

    private String legPay;

    private Double notional;

    private Double fixedRate;

    private String indexTenor;

    private String index;

    @Relationship(type = "PAYS")
    private Set<Leg> payLegs;

    @Relationship(type = "RECEIVE")
    private Set<Leg> receiveLegs;

}
