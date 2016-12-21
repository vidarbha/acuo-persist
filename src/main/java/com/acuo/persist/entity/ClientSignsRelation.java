package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type="CLIENT_SIGNS")
@Data
@EqualsAndHashCode(callSuper = false)
public class ClientSignsRelation extends Entity{

    @StartNode
    private LegalEntity legalEntity;

    @EndNode
    private Agreement agreement;

    private Double initialMarginBalance;

    private Double rounding;

    private Double MTA;

    private Double variationMarginBalance;

}
