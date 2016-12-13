package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.util.Date;
import java.util.Set;

@NodeEntity
public class Valuation extends Entity{

    @DateString(value = "dd/MM/yy")
    private Date date;

    @Override
    public String toString() {
        return "Valuation{" +
                "date=" + date +
                ", values=" + values +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Value> getValues() {
        return values;
    }

    public void setValues(Set<Value> values) {
        this.values = values;
    }

    @Relationship(type = "VALUE")

    private Set<Value> values;


}
