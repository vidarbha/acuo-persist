package com.acuo.persist.entity;

import com.acuo.common.model.margin.Types;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.Set;

@RelationshipEntity(type = "IS_AVAILABLE_FOR")
@Data
@EqualsAndHashCode(callSuper = false)
public class AvailableFor extends Entity<AvailableFor> {

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
