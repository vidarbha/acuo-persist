package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"collateral"})
@ToString(exclude = {"collateral"})
public class CollateralValue extends Value<CollateralValue> {

    private Double amount;

    @Relationship(type = "VALUE", direction = Relationship.INCOMING)
    private Collateral collateral;
}
