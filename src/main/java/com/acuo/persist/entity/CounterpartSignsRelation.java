package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type="COUNTERPARTY_SIGNS")
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"legalEntity", "agreement"})
@ToString(exclude = {"legalEntity", "agreement"})
public class CounterpartSignsRelation extends Entity<CounterpartSignsRelation> {

    @StartNode
    private LegalEntity legalEntity;

    @EndNode
    private Agreement agreement;

    private Double rounding;

    private Double MTA;

    //private Double variationMarginBalance;

    //private Double initialMarginBalance;

    //private Double variationPending;

    //private Double initialPending;

    //private Double initialPendingNonCash;

    //private Double variationPendingNonCash;

}
