package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(exclude = "id")
public class Fund implements Entity<Fund> {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "id")
    @Id
    private String fundId;

    private String name;

    @Relationship(type = "IS_COMPOSED_OF")
    private Set<Portfolio> portfolios;

}
