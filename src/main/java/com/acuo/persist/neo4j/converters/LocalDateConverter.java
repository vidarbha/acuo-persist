package com.acuo.persist.neo4j.converters;

import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String toGraphProperty(LocalDate value) {
        if (Objects.isNull(value)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        return value.format(formatter);
    }

    @Override
    public LocalDate toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        try
        {
            return LocalDate.parse(value,formatter);
        }
        catch (Exception e)
        {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
            return LocalDate.parse(value,formatter);
        }

    }
}