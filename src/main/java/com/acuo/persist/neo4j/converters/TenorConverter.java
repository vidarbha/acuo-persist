package com.acuo.persist.neo4j.converters;

import com.opengamma.strata.basics.date.Tenor;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.Objects;

public class TenorConverter implements AttributeConverter<Tenor, String> {
    @Override
    public String toGraphProperty(Tenor value) {
        if (Objects.isNull(value)) return null;
        return value.toString();
    }

    @Override
    public Tenor toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        return Tenor.parse(value);
    }
}
