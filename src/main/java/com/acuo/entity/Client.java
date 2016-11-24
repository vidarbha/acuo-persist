package com.acuo.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class Client extends Entity {

    private String clientId;

    private String name;

    public String getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    @Relationship(type = "MANAGE")
    private Set<Fund> funds;

    public Set<Fund> getFunds() {
        return funds;
    }
}
