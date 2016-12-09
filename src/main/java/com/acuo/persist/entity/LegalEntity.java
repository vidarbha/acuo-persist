package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
public class LegalEntity extends Entity {

    private String  holidayZone;

    private String name;

    @Property(name="id")
    private String legalEntityId;

    @Override
    public String toString() {
        return "LegalEntity{" +
                "holidayZone='" + holidayZone + '\'' +
                ", name='" + name + '\'' +
                ", legalEntityId='" + legalEntityId + '\'' +
                '}';
    }

    public String getHolidayZone() {
        return holidayZone;
    }

    public void setHolidayZone(String holidayZone) {
        this.holidayZone = holidayZone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalEntityId() {
        return legalEntityId;
    }

    public void setLegalEntityId(String legalEntityId) {
        this.legalEntityId = legalEntityId;
    }
}
