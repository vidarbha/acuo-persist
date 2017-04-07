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
@EqualsAndHashCode(callSuper = false)
public abstract class Valuation extends Entity<Valuation> {

    @Relationship(type = "VALUATED", direction = INCOMING)
    private Portfolio portfolio;
}