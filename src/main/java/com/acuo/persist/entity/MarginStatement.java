package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class MarginStatement extends Entity<MarginStatement> {

    @Property(name="id")
    private String statementId;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime date;

    private Double interestPayment;

    private Double productCashFlows;

    private Double PAI;

    private Double feesCommissions;

    private Double pendingCash;

    private Double pendingNonCash;

    private String direction;

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

    @Relationship(type = "PART_OF", direction = Relationship.INCOMING)
    private Set<MarginCall> marginCalls = new HashSet<>();
}
