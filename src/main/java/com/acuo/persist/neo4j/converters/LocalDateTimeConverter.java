package com.acuo.persist.neo4j.converters;

import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, String> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss.SSS");

    @Override
    public String toGraphProperty(LocalDateTime value) {
        if (Objects.isNull(value)) return null;
        return value.format(formatter);
    }

    @Override
    public LocalDateTime toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        return LocalDateTime.parse(value, formatter);
    }
}