package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
@EqualsAndHashCode(exclude = {"id", "last"})
@ToString(exclude = {"last"})
public class FXRate implements Entity<FXRate>{

    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "FROM")
    private CurrencyEntity from;

    @Relationship(type = "TO")
    private CurrencyEntity to;

    @Relationship(type = "LAST")
    private FXValue last;

}
