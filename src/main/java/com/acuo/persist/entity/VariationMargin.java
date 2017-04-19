package com.acuo.persist.entity;

import com.acuo.common.converter.Converter;
import com.acuo.persist.entity.enums.StatementStatus;
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

    public VariationMargin(Double amount, LocalDate valuationDate, LocalDate callDate, Currency currency, StatementStatus statementStatus, Agreement agreement, Map<Currency, Double> rates) {
        super(amount, valuationDate, callDate, currency, statementStatus, agreement, rates);
        this.marginType = Variation;
        this.marginCallId = marginCallId(agreement, callDate, Variation);
    }

    public static Converter<com.acuo.common.model.margin.MarginCall, VariationMargin> converter = Converter.ofNullable(
            com.acuo.common.model.margin.MarginCall.class,
            VariationMargin.class,
            call -> {
                VariationMargin margin = new VariationMargin();
                return margin;
            },
            variationMargin -> {
                com.acuo.common.model.margin.MarginCall call = new com.acuo.common.model.margin.MarginCall();
                return call;
            }
    );
}