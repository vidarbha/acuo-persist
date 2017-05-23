package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDateTime;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class FXValue extends Entity<FXValue> {

    private Double value;

    private long from;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime lastUpdate;

    @Relationship(type = "OF")
    private FXRate rate;
}
