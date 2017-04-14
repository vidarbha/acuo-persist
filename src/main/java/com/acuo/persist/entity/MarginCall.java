package com.acuo.persist.entity;

import com.acuo.common.util.LocalDateUtils;
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

    @Relationship(type = "PART_OF")
    private MarginStatement marginStatement;

    @Relationship(type = "CHILD_OF", direction = Relationship.INCOMING)
    private Set<ChildOf> children;

    public MarginCall() {
    }

    public MarginCall(Double amount, LocalDate valuationDate, Currency currency, StatementStatus statementStatus, Agreement agreement, Map<Currency, Double> rates) {
        this(convert(amount, currency, agreement.getCurrency(), rates), valuationDate, statementStatus, agreement);
    }

    private MarginCall(Double amount, LocalDate valuationDate, StatementStatus statementStatus, Agreement agreement) {
        this.valuationDate = valuationDate;

        this.callDate = LocalDateUtils.add(valuationDate, 1);

        this.currency = agreement.getCurrency();
        this.parentRank = 0;
        this.notificationTime = callDate.atTime(agreement.getNotificationTime());
        this.status = statementStatus;

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

        Step step = new Step();
        step.setStatus(statementStatus);
        setFirstStep(step);
        setLastStep(step);
    }

    String marginCallId(Agreement agreement, LocalDate valuationDate, MarginType marginType) {
        String todayFormatted = GraphData.getStatementDateFormatter().format(valuationDate);
        return todayFormatted + "-" + agreement.getAgreementId() + "-" + marginType.name();
    }

    private Double balance(ClientSignsRelation clientSignsRelation) {
        return clientSignsRelation.getVariationBalance() != null ? clientSignsRelation.getVariationBalance() : 0.0d;
    }

    private Double pendingCollateral(ClientSignsRelation clientSignsRelation) {
        return clientSignsRelation.getVariationPending() != null ? clientSignsRelation.getVariationPending() : 0.0d;
    }

    private static Double convert(Double value, Currency from, Currency to, Map<Currency, Double> rates) {
        if (from.equals(to)) return value;
        double fromRate = (!from.equals(USD)) ? rates.get(from) : 1;
        double toRate = (!to.equals(USD)) ? rates.get(to) : 1;
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