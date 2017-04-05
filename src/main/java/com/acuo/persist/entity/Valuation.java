package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

import static org.neo4j.ogm.annotation.Relationship.*;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"values"})
@ToString(exclude = {"values"})
public abstract class Valuation<V extends Value> extends Entity<Valuation<V>> {

    @Relationship(type = "VALUE")
    private Set<ValueRelation> values;

    @Relationship(type = "VALUATED", direction = INCOMING)
    private Portfolio portfolio;
}