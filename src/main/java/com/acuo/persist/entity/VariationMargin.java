package com.acuo.persist.entity;

import com.acuo.common.converter.Converter;
import com.acuo.persist.entity.enums.Side;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;

import java.time.LocalDate;
import java.util.Map;

import static com.acuo.common.model.margin.Types.MarginType.Variation;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class VariationMargin extends MarginCall<VariationMargin> {

    public VariationMargin() {
        super();
    }

    public VariationMargin(Side side,
                           Double amount,
                           LocalDate valuationDate,
                           LocalDate callDate,
                           Currency currency,
                           Agreement agreement,
                           MarginStatement marginStatement,
                           Map<Currency, Double> rates,
                           Long tradeCount,
                           Long tradeValued) {
        super(side, amount, valuationDate, callDate, currency, agreement, marginStatement, rates, tradeCount, tradeValued);
        this.marginType = Variation;
        this.itemId = marginCallId(side, agreement, callDate, Variation, direction);
    }

    public static Converter<com.acuo.common.model.margin.MarginCall, VariationMargin> converter = Converter.ofNullable(
            com.acuo.common.model.margin.MarginCall.class,
            VariationMargin.class,
            call -> new VariationMargin(),
            variationMargin -> new com.acuo.common.model.margin.MarginCall()
    );

    protected Double collateralSettled(MarginStatement marginStatement){
        return marginStatement.variationBalance();
    }

    protected Double collateralPending(MarginStatement marginStatement) {
        return marginStatement.variationPending();
    }
}