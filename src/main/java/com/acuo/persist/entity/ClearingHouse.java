package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class ClearingHouse extends Entity<ClearingHouse> {

    @Property(name = "id")
    private String clearingHouseId;
    private String name;
    private String shortName;
    private String jurisdiction;

}
