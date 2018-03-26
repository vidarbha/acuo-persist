package com.acuo.persist.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
public abstract class Valuation<V extends Value> implements Entity<Valuation> {

    @Id
    @GeneratedValue
    private Long id;

}