package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class Agreement extends Entity {

    private String key;

    private String type;

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    @Relationship(type = "COUNTERPARTY_SIGNS", direction = Relationship.INCOMING)
    private Set<LegalEntity> cptyEntitys;

    @Relationship(type = "CLIENT_SIGNS", direction = Relationship.INCOMING)
    private Set<LegalEntity> legalEntitys;

    public void setKey(String key) {
        this.key = key;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<LegalEntity> getCptyEntitys() {
        return cptyEntitys;
    }

    public void setCptyEntitys(Set<LegalEntity> cptyEntitys) {
        this.cptyEntitys = cptyEntitys;
    }

    public Set<LegalEntity> getLegalEntitys() {
        return legalEntitys;
    }

    public void setLegalEntitys(Set<LegalEntity> legalEntitys) {
        this.legalEntitys = legalEntitys;
    }

    @Override
    public String toString() {
        return "Agreement{" +
                "key='" + key + '\'' +
                ", type='" + type + '\'' +
                ", cptyEntitys=" + cptyEntitys +
                ", legalEntitys=" + legalEntitys +
                '}';
    }
}
