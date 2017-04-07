package com.acuo.persist.entity;

import com.acuo.common.model.margin.Types;
import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

import static com.opengamma.strata.basics.currency.Currency.USD;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"marginStatement"})
@ToString(exclude = {"marginStatement"})
public class MarginCall<T extends MarginCall> extends StatementItem<T> {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Property(name = "id")
    private String marginCallId;
    @Convert(LocalDateConverter.class)
    private LocalDate callDate;
    protected Types.MarginType marginType;
    private String direction;
    @Convert(LocalDateConverter.class)
    private LocalDate valuationDate;
    @Convert(CurrencyConverter.class)
    private Currency currency;
    private Double excessAmount;
    private Double balanceAmount;
    private Double deliverAmount;
    private Double returnAmount;
    private Double pendingCollateral;
    private Double exposure;
    private String IMRole;
    private Integer parentRank;
    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime notificationTime;
    private Double roundedReturnAmount;
    private Double roundedDeliverAmount;
    private Integer belowMTA;
    private Double marginAmount;
    private CallStatus status;

    @Relationship(type = "PART_OF")
    private MarginStatement marginStatement;
    @Relationship(type = "FIRST")
    private Step firstStep;
    @Relationship(type = "CHILD_OF", direction = Relationship.INCOMING)
    private Set<ChildOf> children;

    public MarginCall() {
    }

    public MarginCall(TradeValue value, CallStatus callStatus, Agreement agreement, Map<Currency, Double> rates) {
        this.valuationDate = valuationDate(value);
        this.marginCallId = marginCallId(agreement, valuationDate);
        this.callDate = callDate(valuationDate);

        this.currency = agreement.getCurrency();
        this.parentRank = 0;
        this.notificationTime = callDate.atTime(agreement.getNotificationTime());
        this.status = callStatus;

        Double pv = pv(value, agreement.getCurrency(), rates);
        this.balanceAmount = balance(agreement.getClientSignsRelation());
        this.pendingCollateral = pendingCollateral(agreement.getClientSignsRelation());

        this.excessAmount = pv - (balanceAmount + pendingCollateral);

        Double exposure;

        double amount = balanceAmount + pendingCollateral;
        if (excessAmount < 0) {
            exposure = 0 - pv;
            this.direction = "OUT";

        } else {
            exposure = pv;
            this.direction = "IN";
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
        this.exposure = exposure;
    }

    private LocalDate valuationDate(TradeValue value) {
        ValueRelation relation = value.getValuation();
        return relation.getDateTime();
    }

    private String marginCallId(Agreement agreement, LocalDate valuationDate) {
        Types.MarginType marginType = Types.MarginType.Variation;
        String todayFormatted = valuationDate.format(dateTimeFormatter);
        return todayFormatted + "-" + agreement.getAgreementId() + "-" + marginType.name();
    }

    private LocalDate callDate(LocalDate valuationDate) {
        return valuationDate.plusDays(1);
    }

    private Double pv(TradeValue value, Currency targetCurrecy, Map<Currency, Double> rates) {
        Double pv = value.getPv();
        com.opengamma.strata.basics.currency.Currency from = value.getCurrency();
        return convert(pv, from, targetCurrecy, rates);
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