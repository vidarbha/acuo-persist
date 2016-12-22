package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Counterpart extends Entity {

    @Property(name = "id")
    private String counterpartId;

    private String name;

    public String getCounterpartId() {
        return counterpartId;
    }

    public String getName() {
        return name;
    }
}
