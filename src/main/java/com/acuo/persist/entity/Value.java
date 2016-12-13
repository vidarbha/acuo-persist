package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Value extends Entity{

    private Double pv;

    private String currency;

    private String source;

    @Override
    public String toString() {
        return "Value{" +
                "pv=" + pv +
                ", currency='" + currency + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

    public Double getPv() {
        return pv;
    }

    public void setPv(Double pv) {
        this.pv = pv;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
