package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDateTime;

@NodeEntity
@Data
@EqualsAndHashCode(exclude = {"previous"})
@ToString(exclude = "previous")
public class FXValue implements Entity<FXValue> {

    @Id
    @GeneratedValue
    private Long id;

    private Double value;

    private long from;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime lastUpdate;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime refreshedOn;

    @Relationship(type = "OF")
    private FXRate rate;

    @Relationship(type = "PREV")
    private FXValue previous;
}
