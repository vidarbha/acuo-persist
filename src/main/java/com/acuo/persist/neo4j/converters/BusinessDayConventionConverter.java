package com.acuo.persist.neo4j.converters;

import com.opengamma.strata.basics.date.BusinessDayConvention;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.Objects;

public class BusinessDayConventionConverter implements AttributeConverter<BusinessDayConvention, String> {
    @Override
    public String toGraphProperty(BusinessDayConvention value) {
        if (Objects.isNull(value)) return null;
        return value.getName();
    }

    @Override
    public BusinessDayConvention toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        return BusinessDayConvention.of(value);
    }
}
