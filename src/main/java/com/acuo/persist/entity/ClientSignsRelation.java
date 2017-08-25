package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type="CLIENT_SIGNS")
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"legalEntity", "agreement"})
@ToString(exclude = {"legalEntity", "agreement"})
public class ClientSignsRelation extends Entity<ClientSignsRelation> {

    @StartNode
    private LegalEntity legalEntity;

    @EndNode
    private Agreement agreement;

    private Double rounding;

    private Double MTA;

    private Double threshold;

    //private Double variationBalance;

    //private Double initialBalance;

    //private Double variationPending;

    //private Double initialPending;

    //private Double variationPendingNonCash;

    //private Double initialPendingNonCash;

}
