package com.acuo.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Asset extends Entity {

    private String key;

    public String getKey() {
        return key;
    }

    @Relationship(type = "IS_PART_OF")
    private Category category;

    public Category getCategory() {
        return category;
    }
}
