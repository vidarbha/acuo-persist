package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public abstract class Valuation<V extends Value> extends Entity<Valuation> {


}