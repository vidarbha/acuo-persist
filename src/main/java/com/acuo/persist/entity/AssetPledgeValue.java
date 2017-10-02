package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"assetPledge"})
@ToString(exclude = {"assetPledge"})
public class AssetPledgeValue extends Value<AssetPledgeValue>{

    private Double amount;

    @Relationship(type = "VALUE", direction = Relationship.INCOMING)
    private AssetPledge assetPledge;
}
