package com.acuo.persist.entity;

import com.acuo.persist.entity.enums.StatementDirection;
import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.utils.GraphData;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static com.acuo.common.model.margin.Types.MarginType.Initial;
import static com.acuo.common.model.margin.Types.MarginType.Variation;
import static java.util.stream.Collectors.toSet;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class MarginStatement extends Entity<MarginStatement> {

    @Property(name = "id")
    private String statementId;

    @Convert(LocalDateConverter.class)
    private LocalDate date;

    private Double interestPayment;

    private Double productCashFlows;

    private Double PAI;

    private Double feesCommissions;

    private Double pendingCash;

    private Double pendingNonCash;

    private StatementDirection direction;

    private String legalEntityId;

    @Convert(CurrencyConverter.class)
    private Currency currency;

    private Integer reconcileCount;

    private Integer pledgeCount;

    private Integer disputeCount;

    private Double totalAmount;

    private Integer expectedCount;

    private Integer unreconCount;

    private String status;

    @Relationship(type = "STEMS_FROM")
    private Agreement agreement;

    @Relationship(type = "DIRECTED_TO")
    private LegalEntity directedTo;

    @Relationship(type = "SENT_FROM")
    private LegalEntity sentFrom;

    @Relationship(type = "PART_OF", direction = Relationship.INCOMING)
    private Set<StatementItem> statementItems;

    public Set<MarginCall> getMarginCalls() {
        if (statementItems != null)
            return statementItems.stream()
                    .filter(statementItem -> statementItem.getMarginType().equals(Initial)
                            || statementItem.getMarginType().equals(Variation))
                    .map(statementItem -> (MarginCall) statementItem)
                    .collect(toSet());
        else
            return Collections.emptySet();
    }

    public MarginStatement() {
    }

    public MarginStatement(Agreement agreement, LocalDate date, StatementDirection direction) {
        this.agreement = agreement;
        this.statementId = marginStatementId(agreement, date);
        this.direction = direction;
        this.currency = agreement.getCurrency();
        this.date = date;
    }

    private String marginStatementId(Agreement agreement, LocalDate date) {
        String todayFormatted = GraphData.getStatementDateFormatter().format(date);
        return todayFormatted + "-" + agreement.getAgreementId();
    }
}
