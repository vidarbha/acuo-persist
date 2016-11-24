package com.acuo.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class Fund extends Entity {

    private String fundId;
    private String name;

    public String getFundId() {
        return fundId;
    }

    public String getName() {
        return name;
    }

    @Relationship(type = "IS_COMPOSED_OF")
    private Set<Portfolio> portfolios;

    public Set<Portfolio> getPortfolios() {
        return portfolios;
    }
}
