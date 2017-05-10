package com.acuo.persist.entity;

import com.acuo.persist.entity.enums.Side;
import com.acuo.persist.entity.enums.StatementDirection;
import com.acuo.persist.utils.GraphData;
import com.acuo.persist.utils.IDGen;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;

import java.time.LocalDate;
import java.util.Map;

import static com.acuo.common.model.margin.Types.MarginType;
import static com.opengamma.strata.basics.currency.Currency.USD;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public abstract class MarginCall<T extends MarginCall> extends StatementItem<T> {

    private Double excessAmount;
    private Double balanceAmount;
    private Double deliverAmount;
    private Double returnAmount;
    private Double pendingCollateral;
    private Double exposure;
    private String IMRole;
    private Double roundedReturnAmount;
    private Double roundedDeliverAmount;
    private Integer belowMTA;

    private Double fxRate;
    private Long tradeValued;
    private Long tradeCount;

    public MarginCall() {
    }

    public MarginCall(Side side,
                      Double amount,
                      LocalDate valuationDate,
                      LocalDate callDate,
                      Currency currency,
                      Agreement agreement,
                      Map<Currency, Double> rates,
                      Long tradeCount) {
        this(side, convert(amount, currency, agreement.getCurrency(), rates), valuationDate, callDate, agreement);
        this.fxRate = getRate(currency, agreement.getCurrency(), rates);
        this.tradeCount = this.tradeValued = tradeCount;
    }

    private MarginCall(Side side, Double amount, LocalDate valuationDate, LocalDate callDate, Agreement agreement) {
        this.side = side;
        this.valuationDate = valuationDate;
        this.callDate = callDate;
        this.currency = agreement.getCurrency();
        this.parentRank = 0;
        this.notificationTime = callDate.atTime(agreement.getNotificationTime());

        this.balanceAmount = balance(agreement.getClientSignsRelation());
        this.pendingCollateral = pendingCollateral(agreement.getClientSignsRelation());

        this.excessAmount = amount - (balanceAmount + pendingCollateral);

        this.exposure = null;

        double balanceAndPendingAmount = balanceAmount + pendingCollateral;
        if (excessAmount < 0) {
            exposure = 0 - amount;
            this.direction = StatementDirection.OUT;

        } else {
            exposure = amount;
            this.direction = StatementDirection.IN;
        }

        if (sign(exposure) == sign(balanceAndPendingAmount) && exposure > 0) {
            deliverAmount = excessAmount;
            returnAmount = 0d;
        } else if (sign(exposure) == sign(balanceAndPendingAmount) && exposure < 0) {
            deliverAmount = 0d;
            returnAmount = excessAmount;
        } else {
            deliverAmount = exposure;
            returnAmount = Math.abs(balanceAndPendingAmount);
        }

        Double rounding = agreement.getClientSignsRelation().getRounding() != null ? agreement.getClientSignsRelation().getRounding() : 0;
        if (rounding != 0) {
            if (deliverAmount != 0) {
                deliverAmount = deliverAmount / Math.abs(deliverAmount) * Math.ceil(Math.abs(deliverAmount) / rounding) * rounding;
            }
            if (returnAmount != 0) {
                returnAmount = returnAmount / Math.abs(returnAmount) * Math.floor(Math.abs(returnAmount) / rounding) * rounding;
            }
        }
        this.marginAmount = deliverAmount + returnAmount;
    }

    String marginCallId(Side side, Agreement agreement, LocalDate valuationDate, MarginType marginType) {
        String todayFormatted = GraphData.getStatementDateFormatter().format(valuationDate);
        String value = todayFormatted + "-" + agreement.getAgreementId() + "-" + marginType.name() + "-" + side;
        return IDGen.encode(value) ;
    }

    private Double balance(ClientSignsRelation clientSignsRelation) {
        return clientSignsRelation.getVariationBalance() != null ? clientSignsRelation.getVariationBalance() : 0.0d;
    }

    private Double pendingCollateral(ClientSignsRelation clientSignsRelation) {
        return clientSignsRelation.getVariationPending() != null ? clientSignsRelation.getVariationPending() : 0.0d;
    }

    private static Double convert(Double value, Currency from, Currency to, Map<Currency, Double> rates) {
        if (from.equals(to)) return value;
        double rate = getRate(from, to, rates);
        return value * rate;
    }

    private static double getRate(Currency from, Currency to, Map<Currency, Double> rates) {
        double fromRate = (!from.equals(USD)) ? rates.get(from) : 1;
        double toRate = (!to.equals(USD)) ? rates.get(to) : 1;
        return toRate / fromRate;
    }

    private int sign(double d) {
        if (d > 0)
            return 1;
        if (d < 0)
            return -1;
        else
            return 0;
    }
}