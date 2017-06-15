package com.acuo.persist.entity;

import lombok.*;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, exclude={"firm","preferences"})
@ToString(exclude={"firm","preferences"})
public class LegalEntity extends Entity<LegalEntity> {

    @Property(name="id")
    @Index(primary = true)
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

    @Relationship(type = "MANAGES", direction = Relationship.INCOMING)
    private Firm firm;

    @Relationship(type = "ACCESSES")
    private Set<CustodianAccount> custodianAccounts;
}
