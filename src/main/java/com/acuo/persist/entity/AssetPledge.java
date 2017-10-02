package com.acuo.persist.entity;

import com.acuo.common.model.margin.Types;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class AssetPledge extends Entity<AssetPledge> {

    private Types.MarginType marginType;

    private Types.BalanceStatus status;

    @Relationship(type = "OF")
    private Asset asset;

    @Relationship(type = "LATEST")
    private AssetPledgeValue latestValue;

    @Relationship(type = "VALUE")
    private Set<AssetPledgeValue> values;
}
