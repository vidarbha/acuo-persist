package com.acuo.entity;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Rule extends Entity {

    private String type;

    private Double value;

    public Double getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
