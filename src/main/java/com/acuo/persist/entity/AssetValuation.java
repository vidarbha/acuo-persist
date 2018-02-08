package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
@ToString(callSuper = true, exclude = {"latestValue","values"})
@EqualsAndHashCode(callSuper = true, exclude = {"latestValue","values"})
public class AssetValuation extends Valuation<AssetValue> {

    @Relationship(type = "VALUATED")
    private Asset asset;

    @Relationship(type = "LATEST")
    protected AssetValue latestValue;

    @Relationship(type = "VALUE")
    protected Set<AssetValue> values;
}