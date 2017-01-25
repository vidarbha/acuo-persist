package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalTimeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalTime;

@RelationshipEntity(type = "HOLDS")
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"custodian","asset"})
@ToString(exclude = {"custodian","asset"})
public class Holds extends Entity<Holds>{

    @StartNode
    private CustodianAccount custodianAccount;

    @EndNode
    Asset asset;

    @Convert(LocalTimeConverter.class)
    private LocalTime businessTimeFrom;
    @Convert(LocalTimeConverter.class)
    private LocalTime businessTimeTo;
    private Double quantities;
    private Double availableQuantities;
    private Double reservedQuantities;
    /* not implemented on acuo-data yet
    private Double deployedQuantities;
    private Double departingQuantities;
    private Double arrivingQuantities;*/
}
