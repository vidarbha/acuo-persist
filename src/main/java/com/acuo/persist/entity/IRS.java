package com.acuo.persist.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.util.Date;
import java.util.Set;

@NodeEntity
@Data
public class IRS extends Trade{

    @DateString(value = "dd/MM/yy")
    private Date maturity;

    @DateString(value = "dd/MM/yy")
    private Date clearingDate;

    @Property(name="id")
    private String irsId;

    @Relationship(type = "PAYS")
    private Set<Leg> payLegs;

    @Relationship(type = "RECEIVE")
    private Set<Leg> receiveLegs;

    private String tradeType;

    private Account account;
}
