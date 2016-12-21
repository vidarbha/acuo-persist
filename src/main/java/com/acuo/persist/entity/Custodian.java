package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Custodian extends Entity {

    private String custodianId;

    private String name;

    public String getCustodianId() {
        return custodianId;
    }

    public String getName() {
        return name;
    }
}
