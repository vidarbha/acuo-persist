package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
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

    public Set<Fund> getFunds() {
        return funds;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MarginStatement> getMarginStatements() {
        return marginStatements;
    }

    public void setMarginStatements(Set<MarginStatement> marginStatements) {
        this.marginStatements = marginStatements;
    }

    public void setFunds(Set<Fund> funds) {
        this.funds = funds;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId='" + clientId + '\'' +
                ", name='" + name + '\'' +
                ", marginStatements=" + marginStatements +
                ", funds=" + funds +
                '}';
    }
}
