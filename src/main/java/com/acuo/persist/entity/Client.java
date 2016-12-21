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
public class Client extends Entity {

    @Property(name="id")
    private String clientId;

    private String name;

    @Relationship(type = "DIRECTED_TO", direction = Relationship.INCOMING)
    private Set<MarginStatement> marginStatements;

    public String getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    @Relationship(type = "MANAGE")
    private Set<Fund> funds;

}
