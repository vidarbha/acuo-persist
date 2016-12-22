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
public class LegalEntity extends Entity {

    private String  holidayZone;

    private String name;

    @Property(name="id")
    private String legalEntityId;

    @Relationship(type = "CLIENT_SIGNS")
    private Set<ClientSignsRelation> clientSignses;
}
