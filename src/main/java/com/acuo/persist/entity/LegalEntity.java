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

    @Property(name="id")
    private String legalEntityId;

    private String name;

    private String  holidayZone;

    private String shortName;

    @Relationship(type = "PREFERENCES")
    private Set<Preferences> preferences;

    @Relationship(type = "CLIENT_SIGNS")
    private Set<ClientSignsRelation> clientSignsRelations;

    @Relationship(type = "COUNTERPARTY_SIGNS")
    private Set<CounterpartSignsRelation> counterpartSignsRelations;

    @Relationship(type = "DIRECTED_TO", direction = Relationship.INCOMING)
    private Set<MarginStatement> marginStatements;
}
