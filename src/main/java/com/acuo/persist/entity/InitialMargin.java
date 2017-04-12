package com.acuo.persist.entity;

import com.acuo.persist.entity.enums.StatementStatus;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;

import java.time.LocalDate;
import java.util.Map;

import static com.acuo.common.model.margin.Types.MarginType.Initial;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class InitialMargin extends MarginCall<InitialMargin> {

    private Double exchangeRequirement;
    private Double brokerRequirement;
    private Double initialBalanceCash;
    private Double initialBalanceNonCash;
    private String IMRole;

    public InitialMargin() {}

    public InitialMargin(Double value, LocalDate valuationDate, Currency currency, StatementStatus statementStatus, Agreement agreement, Map<Currency, Double> rates) {
        super(value, valuationDate, currency, statementStatus, agreement, rates);
        this.marginType = Initial;
        this.marginCallId = marginCallId(agreement, valuationDate, Initial);
    }
}
