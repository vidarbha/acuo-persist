package com.acuo.persist.entity;

import com.acuo.persist.entity.enums.StatementDirection;
import com.acuo.persist.entity.enums.StatementStatus;
import com.acuo.persist.utils.GraphData;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static com.acuo.common.model.margin.Types.MarginType;
import static com.opengamma.strata.basics.currency.Currency.USD;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"marginStatement"})
@ToString(exclude = {"marginStatement"})
public abstract class MarginCall<T extends MarginCall> extends StatementItem<T> {

    @Property(name = "id")
    protected String marginCallId;

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
    private Double marginAmount;

    @Relationship(type = "PART_OF")
    private MarginStatement marginStatement;

    @Relationship(type = "CHILD_OF", direction = Relationship.INCOMING)
    private Set<ChildOf> children;

    public MarginCall() {
    }

    public MarginCall(TradeValue value, StatementStatus statementStatus, Agreement agreement, Map<Currency, Double> rates) {
        this.valuationDate = valuationDate(value);

        this.callDate = callDate(valuationDate);

        this.currency = agreement.getCurrency();
        this.parentRank = 0;
        this.notificationTime = callDate.atTime(agreement.getNotificationTime());
        this.status = statementStatus;

        Double pv = pv(value, agreement.getCurrency(), rates);
        this.balanceAmount = balance(agreement.getClientSignsRelation());
        this.pendingCollateral = pendingCollateral(agreement.getClientSignsRelation());

        this.excessAmount = pv - (balanceAmount + pendingCollateral);

        this.exposure = null;

        double amount = balanceAmount + pendingCollateral;
        if (excessAmount < 0) {
            exposure = 0 - pv;
            this.direction = StatementDirection.OUT;

        } else {
            exposure = pv;
            this.direction = StatementDirection.IN;
        }

        if (sign(exposure) == sign(amount) && exposure > 0) {
            deliverAmount = marginAmount;
            returnAmount = 0d;
        } else if (sign(exposure) == sign(amount) && exposure < 0) {
            deliverAmount = 0d;
            returnAmount = marginAmount;
        } else {
            deliverAmount = exposure;
            returnAmount = Math.abs(amount);
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

        Step step = new Step();
        step.setStatus(statementStatus);
        setFirstStep(step);
        setLastStep(step);
    }

    protected String marginCallId(Agreement agreement, LocalDate valuationDate, MarginType marginType) {
        String todayFormatted = GraphData.getStatementDateFormatter().format(valuationDate);
        return todayFormatted + "-" + agreement.getAgreementId() + "-" + marginType.name();
    }

    private LocalDate valuationDate(TradeValue value) {
        ValueRelation relation = value.getValuation();
        return relation.getDateTime();
    }

    private LocalDate callDate(LocalDate valuationDate) {
        return valuationDate.plusDays(1);
    }

    private Double pv(TradeValue value, Currency targetCurrency, Map<Currency, Double> rates) {
        Double pv = value.getPv();
        com.opengamma.strata.basics.currency.Currency from = value.getCurrency();
        return convert(pv, from, targetCurrency, rates);
    }

    private Double balance(ClientSignsRelation clientSignsRelation) {
        return clientSignsRelation.getVariationBalance() != null ? clientSignsRelation.getVariationBalance() : 0.0d;
    }

    private Double pendingCollateral(ClientSignsRelation clientSignsRelation) {
        return clientSignsRelation.getVariationPending() != null ? clientSignsRelation.getVariationPending() : 0.0d;
    }

    private Double convert(Double value, Currency from, Currency to, Map<Currency, Double> rates) {
        if (from.equals(to)) return value;
        double fromRate = (!from.equals(USD)) ? rates.get(from.getCode()) : 1;
        double toRate = (!to.equals(USD)) ? rates.get(to.getCode()) : 1;
        return value * toRate / fromRate;
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