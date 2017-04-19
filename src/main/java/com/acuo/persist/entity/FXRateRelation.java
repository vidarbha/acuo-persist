package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.Convert;

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

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime lastUpdate;


}
