package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Asset extends Entity {

    private String key;

    @Relationship(type = "IS_PART_OF")
    private Category category;

}
