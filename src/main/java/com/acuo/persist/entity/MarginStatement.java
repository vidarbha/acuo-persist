package com.acuo.persist.entity;

import com.acuo.common.model.margin.Types.AssetType;
import com.acuo.common.model.margin.Types.BalanceStatus;
import com.acuo.common.model.margin.Types.MarginType;
import com.acuo.persist.entity.enums.StatementDirection;
import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.utils.GraphData;
import com.acuo.persist.utils.IDGen;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiPredicate;

import static com.acuo.common.model.margin.Types.AssetType.Cash;
import static com.acuo.common.model.margin.Types.AssetType.NonCash;
import static com.acuo.common.model.margin.Types.BalanceStatus.Pending;
import static com.acuo.common.model.margin.Types.BalanceStatus.Settled;
import static com.acuo.common.model.margin.Types.MarginType.Initial;
import static com.acuo.common.model.margin.Types.MarginType.Variation;
import static com.acuo.common.util.ArithmeticUtils.addition;
import static java.util.stream.Collectors.toSet;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"collaterals"})
@ToString(exclude = {"collaterals"})
public class MarginStatement extends Entity<MarginStatement> {

    @Property(name = "id")
    @Index(primary = true)
    private String statementId;

    @Convert(LocalDateConverter.class)
    private LocalDate date;

    private Double interestPayment;

    private Double productCashFlows;

    private Double PAI;

    private Double feesCommissions;

    private Double pendingCash;

    private Double pendingNonCash;

    //private StatementDirection direction;

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

    @Relationship(type = "BALANCE", direction = Relationship.INCOMING)
    private Set<Collateral> collaterals = new HashSet<>();

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

    public MarginStatement(Agreement agreement, LocalDate callDate/*, StatementDirection direction*/) {
        this.agreement = agreement;
        this.statementId = marginStatementId(agreement, callDate);
        //this.direction = direction;
        this.currency = agreement.getCurrency();
        this.date = callDate;
        ClientSignsRelation clientSignsRelation = agreement.getClientSignsRelation();
        CounterpartSignsRelation counterpartSignsRelation = agreement.getCounterpartSignsRelation();
        LegalEntity client = clientSignsRelation.getLegalEntity();
        LegalEntity counterpart = counterpartSignsRelation.getLegalEntity();
        //if (direction.equals(StatementDirection.IN)) {
            this.setDirectedTo(counterpart);
            this.setSentFrom(client);
        //} else {
        //    this.setDirectedTo(client);
        //    this.setSentFrom(counterpart);
        //}
        this.setPendingCash(addition(initialPending(), variationPending()));
        this.setPendingNonCash(addition(initialPendingNonCash(), variationPendingNonCash()));
    }

    private String marginStatementId(Agreement agreement, LocalDate date) {
        String todayFormatted = GraphData.getStatementDateFormatter().format(date);
        String value = todayFormatted + "-" + agreement.getAgreementId();
        return IDGen.encode(value);
    }

    private static BiPredicate<Object[], Object> contains = (us, u) -> Arrays.asList(us).contains(u);

    private double collateral(MarginType[] marginTypes,
                              AssetType[] assetTypes,
                              BalanceStatus[] balanceStatuses) {
        return collaterals.stream()
                .filter(collateral -> contains.test(marginTypes, collateral.getMarginType()))
                .filter(collateral -> contains.test(assetTypes, collateral.getAssetType()))
                .filter(collateral -> contains.test(balanceStatuses, collateral.getStatus()))
                .map(Collateral::getLatestValue)
                .mapToDouble(CollateralValue::getAmount)
                .sum();
    }

    public Double variationBalance() {
        return collateral(new MarginType[]{Variation},
                          new AssetType[]{Cash, NonCash},
                          new BalanceStatus[]{Settled});
    }

    public Double initialBalance() {
        return collateral(new MarginType[]{Initial},
                new AssetType[]{Cash, NonCash},
                new BalanceStatus[]{Settled});
    }

    public Double variationPending() {
        return collateral(new MarginType[]{Variation},
                new AssetType[]{Cash, NonCash},
                new BalanceStatus[]{Pending});
    }

    public Double initialPending() {
        return collateral(new MarginType[]{Initial},
                new AssetType[]{Cash, NonCash},
                new BalanceStatus[]{Pending});
    }

    public Double variationPendingNonCash() {
        return collateral(new MarginType[]{Variation},
                new AssetType[]{NonCash},
                new BalanceStatus[]{Pending});
    }

    public Double initialPendingNonCash() {
        return collateral(new MarginType[]{Initial},
                new AssetType[]{NonCash},
                new BalanceStatus[]{Pending});
    }
}
