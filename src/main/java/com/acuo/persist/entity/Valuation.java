package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
@EqualsAndHashCode(exclude = {"id"})
public abstract class Valuation<V extends Value> implements Entity<Valuation> {

    @Id
    @GeneratedValue
    private Long id;

}