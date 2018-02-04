package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity(label = "Currency")
@Data
@EqualsAndHashCode(callSuper = false)
public class CurrencyEntity extends Entity<CurrencyEntity> {

    @Property(name = "id")
    @Id
    private String currencyId;

    private String name;
}
