package com.acuo.persist.neo4j.converters;

import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.time.Instant;

public class InstantConverter implements AttributeConverter<Instant, Long> {

    @Override
    public Long toGraphProperty(Instant value) {
        return value.toEpochMilli();
    }

    @Override
    public Instant toEntityAttribute(Long value) {
        return Instant.ofEpochMilli(value);
    }
}
