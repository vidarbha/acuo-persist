package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalTimeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalTime;

@RelationshipEntity(type = "HOLDS")
@Data
@EqualsAndHashCode(exclude = {"id", "custodianAccount","asset"})
@ToString(exclude = {"custodianAccount","asset"})
public class Holds implements Entity<Holds>{

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private CustodianAccount custodianAccount;

    @EndNode
    Asset asset;

    @Convert(LocalTimeConverter.class)
    private LocalTime businessTimeFrom;
    @Convert(LocalTimeConverter.class)
    private LocalTime businessTimeTo;
    private Double availableQuantity;
    private Double reservedQuantity;
    private Double internalCost;
    private Double opptCost;
    /* not implemented on acuo-data yet
    private Double deployedQuantity;
    private Double departingQuantity;
    private Double arrivingQuantity;*/
}
