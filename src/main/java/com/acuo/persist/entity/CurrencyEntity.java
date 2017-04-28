package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label = "Currency")
@Data
@EqualsAndHashCode(callSuper = false, exclude={"fxRateRelation"})
@ToString(exclude = {"fxRateRelation"})
public class CurrencyEntity extends Entity<CurrencyEntity> {

    @Property(name = "id")
    @Index(unique = true)
    private String currencyId;

    private String name;

    @Relationship(type = "FX_RATE")
    private FXRateRelation fxRateRelation;
}
