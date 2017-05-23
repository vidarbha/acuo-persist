package com.acuo.persist.entity;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Getter
@Setter
public class FXRate extends Entity<FXRate>{

    @Relationship(type = "FROM")
    private CurrencyEntity from;

    @Relationship(type = "TO")
    private CurrencyEntity to;

}
