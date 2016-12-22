package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Firm extends Entity<Firm> {

    @Property(name="id")
    private String firmId;

    private String name;

    private String shortName;

    @Relationship(type = "MANAGES")
    private Set<LegalEntity> legalEntities;
}
