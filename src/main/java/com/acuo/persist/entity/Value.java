package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class Value<T extends Value> extends Entity<T> {

    @Relationship(type = "VALUE", direction = INCOMING)
    private ValueRelation<T> valuation;

    private String source;
}
