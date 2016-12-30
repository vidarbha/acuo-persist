package com.acuo.persist.entity;

import com.acuo.common.model.margin.Types;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.Set;

@RelationshipEntity(type="IS_ELIGIBLE_UNDER")
@Getter
@Setter
public class EligibleUnder extends Entity<EligibleUnder> {

    @Property(name="callType")
    private Set<Types.CallType> callTypes;
    private Double haircut;
    @Property(name="FXHaircut")
    private Double fxHaircut;
    private Double externalCost;
    private Double interestRate;
    @Property(name="FXRate")
    private Double fxRate;
}
