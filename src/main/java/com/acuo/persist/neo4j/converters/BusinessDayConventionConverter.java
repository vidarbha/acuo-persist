package com.acuo.persist.neo4j.converters;

import com.opengamma.strata.basics.date.BusinessDayConvention;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.Objects;

@Slf4j
public class BusinessDayConventionConverter implements AttributeConverter<BusinessDayConvention, String> {
    @Override
    public String toGraphProperty(BusinessDayConvention value) {
        if (Objects.isNull(value)) return null;
        return value.getName();
    }

    @Override
    public BusinessDayConvention toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        try
        {
            return BusinessDayConvention.of(value);
        }
        catch (Exception e)
        {
            log.error("error in BusinessDayConvention:", e);
            return null;
        }

    }
}
