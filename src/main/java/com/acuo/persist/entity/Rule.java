package com.acuo.persist.entity;

import com.acuo.common.model.margin.Types;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class Rule extends Entity<Rule> {

    @Property(name="marginType")
    private Set<Types.MarginType> marginTypes;
    private Double haircut;
    @Property(name="FXHaircut")
    private Double fxHaircut;
    private Double externalCost;
    private Double interestRate;
    @Property(name="FXRate")
    private Double fxRate;
}
