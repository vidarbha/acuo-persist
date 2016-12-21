package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class ClearingHouse extends Entity {

    private String clearingHouseId;
    private String lei;
    private String name;

    public String getClearingHouseId() {
        return clearingHouseId;
    }

    public String getLei() {
        return lei;
    }

    public String getName() {
        return name;
    }
}
