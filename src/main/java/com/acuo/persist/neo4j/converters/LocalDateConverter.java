package com.acuo.persist.neo4j.converters;

import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateConverter implements AttributeConverter<LocalDate, String> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Override
    public String toGraphProperty(LocalDate value) {
        if (Objects.isNull(value)) return null;
        return value.format(formatter);
    }

    @Override
    public LocalDate toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        return LocalDate.parse(value,formatter);
    }
}