package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Custodian extends Entity<Custodian> {

    @Property(name = "id")
    @Index(primary = true)
    private String custodianId;
    private String country;
    private String countryShortName;

    @Relationship(type = "MANAGES")
    private Set<CustodianAccount> custodianAccounts;

}
