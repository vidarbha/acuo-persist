package com.acuo.persist.neo4j.converters;

import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.time.LocalDate;
import java.util.Objects;

public class LocalDateConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String toGraphProperty(LocalDate value) {
        if (Objects.isNull(value)) return null;
        return value.toString();
    }

    @Override
    public LocalDate toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        return LocalDate.parse(value);
    }
}