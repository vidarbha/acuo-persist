package com.acuo.persist.neo4j.converters;

import com.opengamma.strata.basics.schedule.Frequency;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.Objects;

public class FrequencyConverter implements AttributeConverter<Frequency, String> {
    @Override
    public String toGraphProperty(Frequency value) {
        if (Objects.isNull(value)) return null;
        return value.toString();
    }

    @Override
    public Frequency toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        return Frequency.parse(value);
    }
}
