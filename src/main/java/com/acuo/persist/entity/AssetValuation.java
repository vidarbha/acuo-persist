package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"values"})
@ToString(exclude = {"values"})
public class AssetValuation extends Valuation{

    @Relationship(type = "VALUATED")
    private Asset asset;

    @Relationship(type = "VALUE")
    private Set<AssetValue> values;
}