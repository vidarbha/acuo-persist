package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"custodian"})
@ToString(exclude = {"custodian"})
public class CustodianAccount extends Entity<CustodianAccount> {

    @Property(name = "id")
    @Id
    private String accountId;
    private String name;
    private String region;

    @Relationship(type = "MANAGES", direction = INCOMING)
    private Custodian custodian;

}
