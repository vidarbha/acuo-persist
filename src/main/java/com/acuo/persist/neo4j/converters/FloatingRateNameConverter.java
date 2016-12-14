package com.acuo.persist.neo4j.converters;

import com.opengamma.strata.basics.index.FloatingRateName;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.Objects;

public class FloatingRateNameConverter implements AttributeConverter<FloatingRateName, String> {
    @Override
    public String toGraphProperty(FloatingRateName value) {
        if (Objects.isNull(value)) return null;
        return value.getName();
    }

    @Override
    public FloatingRateName toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        return FloatingRateName.of(value);
    }
}
