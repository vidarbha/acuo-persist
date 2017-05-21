package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDateTime;

@NodeEntity
@Getter
@Setter
public class FXRate extends Entity<FXRate>{

    @Relationship(type = "FROM")
    private CurrencyEntity from;

    @Relationship(type = "TO")
    private CurrencyEntity to;

    private Double value;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime lastUpdate;

}
