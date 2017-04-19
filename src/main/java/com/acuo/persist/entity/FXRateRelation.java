package com.acuo.persist.entity;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import java.time.LocalDateTime;

@RelationshipEntity(type="FX_RATE")
@Getter
@Setter
public class FXRateRelation  extends Entity<FXRateRelation>{

    @StartNode
    private CurrencyEntity from;

    @EndNode
    private CurrencyEntity to;

    private Double fxRate;

    private LocalDateTime lastUpdate;


}
