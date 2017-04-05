package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"values"})
@ToString(exclude = {"values"})
public class Valuation<V extends Value> extends Entity<Valuation<V>> {

    @Property(name = "id")
    private String valuationId;

    @Relationship(type = "VALUE")
    private Set<ValueRelation<V>> values;

    @Relationship(type = "VALUATED", direction = Relationship.INCOMING)
    private Portfolio portfolio;

}
