package com.acuo.persist.neo4j.converters;

import com.opengamma.strata.basics.date.DayCount;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.Objects;

public class DayCountConverter implements AttributeConverter<DayCount, String> {
    @Override
    public String toGraphProperty(DayCount value) {
        if (Objects.isNull(value)) return null;
        return value.getName();
    }

    @Override
    public DayCount toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        return DayCount.of(value);
    }
}
