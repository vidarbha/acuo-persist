package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"asset"})
@ToString(exclude = {"asset"})
public class AssetValuation extends Valuation<AssetValue>{

    @Relationship(type = "VALUATED", direction = Relationship.INCOMING)
    private Asset asset;
}