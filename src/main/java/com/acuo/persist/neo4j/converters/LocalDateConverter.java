package com.acuo.persist.neo4j.converters;

import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.time.LocalDate;

public class LocalDateConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String toGraphProperty(LocalDate value) {
        return value.toString();
    }

    @Override
    public LocalDate toEntityAttribute(String value) {
        return LocalDate.parse(value);
    }
}