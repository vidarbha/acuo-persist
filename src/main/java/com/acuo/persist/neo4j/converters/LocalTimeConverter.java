package com.acuo.persist.neo4j.converters;

import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.time.LocalTime;
import java.util.Objects;

public class LocalTimeConverter implements AttributeConverter<LocalTime, String> {
    
    @Override
    public String toGraphProperty(LocalTime value) {
        if (Objects.isNull(value)) return null;
        return value.toString();
    }

    @Override
    public LocalTime toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        return LocalTime.parse(value);
    }
}