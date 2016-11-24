package com.acuo.entity;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Agreement extends Entity {

    private String key;

    private String type;

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }
}
