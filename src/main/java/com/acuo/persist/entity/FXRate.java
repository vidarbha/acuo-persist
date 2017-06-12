package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
@ToString(exclude = {"last"})
@EqualsAndHashCode(callSuper = false, exclude = {"last"})
public class FXRate extends Entity<FXRate>{

    @Relationship(type = "FROM")
    private CurrencyEntity from;

    @Relationship(type = "TO")
    private CurrencyEntity to;

    @Relationship(type = "LAST")
    private FXValue last;

}
