package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public abstract class Entity<T extends Entity> {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || id == null || getClass() != o.getClass()) return false;

        T entity = (T) o;

        return id.equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return (id == null) ? -1 : id.hashCode();
    }
}