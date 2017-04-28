package com.acuo.persist.entity;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type="CLIENT_SIGNS")
@Getter
@Setter
public class ClientSignsRelation extends Entity<ClientSignsRelation> {

    @StartNode
    private LegalEntity legalEntity;

    @EndNode
    private Agreement agreement;

    private Double initialBalance;

    private Double rounding;

    private Double MTA;

    private Double variationBalance;

    private Double variationPending;

    private Double initialPending;

    private Double threshold;

    private Double initialPendingNonCash;

    private Double variationPendingNonCash;

    @Override
    public String toString() {
        return "ClientSignsRelation{" +
                "initialBalance=" + initialBalance +
                ", rounding=" + rounding +
                ", MTA=" + MTA +
                ", variationBalance=" + variationBalance +
                ", variationPending=" + variationPending +
                ", initialPending=" + initialPending +
                ", threshold=" + threshold +
                ", initialPendingNonCash=" + initialPendingNonCash +
                ", variationPendingNonCash=" + variationPendingNonCash +
                '}';
    }
}
