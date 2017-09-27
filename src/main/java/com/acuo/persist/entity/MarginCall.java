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
import java.time.LocalDateTime;
import java.util.Map;

import static com.acuo.common.model.margin.Types.MarginType;
import static com.opengamma.strata.basics.currency.Currency.USD;
import static java.lang.Math.*;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
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
    private String ampId;
    private Integer sentMS;

    protected LocalDateTime modifiedDate;

    public MarginCall() {
    }

    public MarginCall(Side side,
                      Double amount,
                      LocalDate valuationDate,
                      LocalDate callDate,
                      Currency currency,
                      Agreement agreement,
                      MarginStatement marginStatement,
                      Map<Currency, Double> rates,
                      Long tradeCount) {
        this(side, convert(amount, currency, agreement.getCurrency(), rates), valuationDate, callDate, agreement, marginStatement);
        this.fxRate = getRate(currency, agreement.getCurrency(), rates);
        this.tradeCount = this.tradeValued = tradeCount;
    }

    abstract Double collateralSettled(MarginStatement marginStatement);

    abstract Double collateralPending(MarginStatement marginStatement);
    
    private MarginCall(Side side,
                       Double amount,
                       LocalDate valuationDate,
                       LocalDate callDate,
                       Agreement agreement,
                       MarginStatement marginStatement) {
        this.side = side;
        this.valuationDate = valuationDate;
        this.callDate = callDate;
        this.currency = agreement.getCurrency();
        this.parentRank = 0;
        this.notificationTime = callDate.atTime(agreement.getNotificationTime());

        this.balanceAmount = collateralSettled(marginStatement);
        this.pendingCollateral = collateralPending(marginStatement);

        this.exposure = amount;

        double balanceAndPendingAmount = balanceAmount + pendingCollateral;

        this.excessAmount = amount - balanceAndPendingAmount;

        if (excessAmount < 0) {
            this.direction = StatementDirection.OUT;
        } else {
            this.direction = StatementDirection.IN;
        }

        if (sign(exposure) == sign(balanceAndPendingAmount))
            if(sign(excessAmount) == sign(balanceAndPendingAmount)) {
                deliverAmount = abs(excessAmount);
                returnAmount = 0d;
            } else {
                deliverAmount = 0d;
                returnAmount = abs(excessAmount);
        } else {
            deliverAmount = abs(exposure);
            returnAmount = abs(balanceAndPendingAmount);
        }

        Double rounding = agreement.getClientSignsRelation().getRounding() != null ? agreement.getClientSignsRelation().getRounding() : 0;
        if (rounding != 0) {
            if (deliverAmount != 0) {
                deliverAmount = deliverAmount / abs(deliverAmount) * ceil(abs(deliverAmount) / rounding) * rounding;
            }
            if (returnAmount != 0) {
                returnAmount = returnAmount / abs(returnAmount) * floor(abs(returnAmount) / rounding) * rounding;
            }
        }
        this.marginAmount = deliverAmount + returnAmount;
    }

    String marginCallId(Side side, Agreement agreement, LocalDate valuationDate, MarginType marginType, StatementDirection direction) {
        String todayFormatted = GraphData.getStatementDateFormatter().format(valuationDate);
        String value = todayFormatted + "-" + agreement.getAgreementId() + "-" + marginType.name() + "-" + side + "-" + direction;
        return IDGen.encode(value) ;
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