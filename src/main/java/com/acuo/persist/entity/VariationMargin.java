package com.acuo.persist.entity;

import com.acuo.persist.entity.enums.StatementStatus;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.Map;

import static com.acuo.common.model.margin.Types.MarginType.Variation;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class VariationMargin extends MarginCall<VariationMargin> {

    public VariationMargin() {
        super();
    }

    public VariationMargin(TradeValue value, StatementStatus statementStatus, Agreement agreement, Map<Currency, Double> rates) {
        super(value, statementStatus, agreement, rates);
        this.marginType = Variation;
    }
}