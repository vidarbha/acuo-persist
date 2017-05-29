package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDateTime;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class Value<T extends Value> extends Entity<T> {

    private String source;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime dateTime;

}