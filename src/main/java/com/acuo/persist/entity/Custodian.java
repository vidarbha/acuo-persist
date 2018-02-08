package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(exclude = {"id"})
public class Custodian implements Entity<Custodian> {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "id")
    @Id
    private String custodianId;
    private String country;
    private String countryShortName;

    @Relationship(type = "MANAGES")
    private Set<CustodianAccount> custodianAccounts = new HashSet<>();

}
