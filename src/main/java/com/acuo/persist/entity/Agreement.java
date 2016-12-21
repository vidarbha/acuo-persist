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
public class Agreement extends Entity {

    private String key;

    private String type;

    @Property(name="id")
    private String agreementId;

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    @Relationship(type = "COUNTERPARTY_SIGNS", direction = Relationship.INCOMING)
    private Set<LegalEntity> cptyEntitys;

    //private Set<ClientSignsRelation> clientSignses;

//    @Relationship(type = "ClientSignsRelation", direction = Relationship.INCOMING)
//    private LegalEntity legalEntity;

}
