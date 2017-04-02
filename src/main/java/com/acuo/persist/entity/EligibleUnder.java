package com.acuo.persist.entity;

import com.acuo.common.model.margin.Types;
import lombok.Data;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import java.util.Set;

@RelationshipEntity(type="IS_ELIGIBLE_UNDER")
@Data
public class EligibleUnder extends Entity<EligibleUnder> {

    @StartNode
    private AssetCategory assetCategory;

    @EndNode
    private Agreement agreement;

    @Property(name="callType")
    private Set<Types.CallType> callTypes;

    private Double haircut;

    @Property(name="FXHaircut")
    private Double fxHaircut;

    private Double externalCost;

    private Double interestRate;

    /*@Property(name="FXRate")
    private Double fxRate;*/
}