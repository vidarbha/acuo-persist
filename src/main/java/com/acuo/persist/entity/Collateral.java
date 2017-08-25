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
public class Collateral extends Entity<Collateral>{

    private Types.MarginType marginType;

    private Types.AssetType assetType;

    private Types.BalanceStatus status;

    @Relationship(type = "BALANCE")
    private MarginStatement statement;

    @Relationship(type = "LATEST")
    private CollateralValue latestValue;

    @Relationship(type = "VALUE")
    private Set<CollateralValue> values;

}
