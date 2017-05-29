package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.InstantConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.Instant;
import java.time.LocalDate;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class Value<T extends Value> extends Entity<T> {

    private String source;

    @Convert(LocalDateConverter.class)
    private LocalDate valuationDate;

    @Convert(InstantConverter.class)
    private Instant timestamp;

}