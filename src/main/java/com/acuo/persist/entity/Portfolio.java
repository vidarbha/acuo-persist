package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@NodeEntity
public class Portfolio extends Entity {

    private String portfolioId;
    private String name;
    private String currency;

    public String getPortfolioId() {
        return portfolioId;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    @Relationship(type = "IS_COMPOSED_OF")
    private Set<Exposure> exposures;

    public Set<Exposure> getExposures() {
        return Optional.ofNullable(exposures).orElseGet(Collections::emptySet);
    }

    @Relationship(type = "IS_COMPOSED_OF")
    private Set<Asset> assets;

    public Set<Asset> getAssets() {
        return Optional.ofNullable(assets).orElseGet(Collections::emptySet);
    }
}
