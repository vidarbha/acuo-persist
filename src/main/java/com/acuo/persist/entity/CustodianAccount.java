package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class CustodianAccount extends Entity<CustodianAccount> {

    @Property(name = "id")
    private String accountId;
    private String name;
    private String region;


}
