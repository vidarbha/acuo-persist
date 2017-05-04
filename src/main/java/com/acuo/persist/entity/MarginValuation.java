package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@NodeEntity
@Data
@ToString(callSuper = true, exclude = {"values"})
@EqualsAndHashCode(callSuper = true, exclude = {"values"})
public class MarginValuation extends Valuation {

    @Relationship(type = "VALUE")
    private Set<MarginValueRelation> values;

    @Relationship(type = "VALUATED", direction = INCOMING)
    private Portfolio portfolio;
}