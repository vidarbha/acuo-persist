package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Portfolio extends Entity<Portfolio> {

    @Property(name="id")
    private String portfolioId;

    private String name;

    private String currency;

    @Relationship(type = "IS_COMPOSED_OF")
    private Set<Exposure> exposures;

    @Relationship(type = "IS_COMPOSED_OF")
    private Set<Asset> assets;

    @Relationship(type="VALUATED")
    private Set<Valuation> valuations;

    @Relationship(type="FOLLOWS")
    private Agreement agreement;

}
