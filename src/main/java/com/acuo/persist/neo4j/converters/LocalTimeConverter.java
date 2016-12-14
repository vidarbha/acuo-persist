package com.acuo.persist.neo4j.converters;

import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.time.LocalTime;

public class LocalTimeConverter implements AttributeConverter<LocalTime, String> {
    
    @Override
    public String toGraphProperty(LocalTime value) {
        return value.toString();
    }

    @Override
    public LocalTime toEntityAttribute(String value) {
        if (value != null && !"null".equals(value)) {
            return LocalTime.parse(value);
        }  else {
            return null;
        }
    }
}