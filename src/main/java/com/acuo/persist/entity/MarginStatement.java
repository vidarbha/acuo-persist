package com.acuo.persist.entity;

import com.acuo.common.util.LocalDateUtils;
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
import static com.acuo.common.util.ArithmeticUtils.addition;
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

    public MarginStatement(Agreement agreement, LocalDate callDate, StatementDirection direction) {
        this.agreement = agreement;
        this.statementId = marginStatementId(agreement, callDate);
        this.direction = direction;
        this.currency = agreement.getCurrency();
        this.date = callDate;
        ClientSignsRelation clientSignsRelation = agreement.getClientSignsRelation();
        CounterpartSignsRelation counterpartSignsRelation = agreement.getCounterpartSignsRelation();
        LegalEntity client = clientSignsRelation.getLegalEntity();
        LegalEntity counterpart = counterpartSignsRelation.getLegalEntity();
        if (direction.equals(StatementDirection.IN)) {
            this.setDirectedTo(counterpart);
            this.setSentFrom(client);
            this.setPendingCash(addition(clientSignsRelation.getInitialPending(), clientSignsRelation.getVariationPending()));
            this.setPendingNonCash(addition(clientSignsRelation.getInitialPendingNonCash(), clientSignsRelation.getVariationPendingNonCash()));
        } else {
            this.setDirectedTo(client);
            this.setSentFrom(counterpart);
            this.setPendingCash(addition(counterpartSignsRelation.getInitialPending(), counterpartSignsRelation.getVariationPending()));
            this.setPendingNonCash(addition(counterpartSignsRelation.getInitialPendingNonCash(), counterpartSignsRelation.getVariationPendingNonCash()));
        }
    }

    private String marginStatementId(Agreement agreement, LocalDate date) {
        String todayFormatted = GraphData.getStatementDateFormatter().format(date);
        return todayFormatted + "-" + agreement.getAgreementId();
    }
}
