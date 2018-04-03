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
@EqualsAndHashCode(exclude = {"last"})
@ToString(exclude = {"last"})
public class FXRate implements Entity<FXRate>{

    public static FXRate USD_RATE = new FXRate();
    static {
        CurrencyEntity USD = new CurrencyEntity();
        USD.setCurrencyId("USD");
        USD.setName("USD");
        USD_RATE.setFrom(USD);
        USD_RATE.setTo(USD);
        FXValue value = new FXValue();
        value.setValue(1d);
        USD_RATE.setLast(value);
    }

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
