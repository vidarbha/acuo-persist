package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"values", "asset"})
@ToString(exclude = {"values", "asset"})
public class AssetValuation extends Valuation{

    @Relationship(type = "VALUE")
    private Set<AssetValueRelation> values;

    @Relationship(type = "VALUATED", direction = Relationship.INCOMING)
    private Asset asset;
}