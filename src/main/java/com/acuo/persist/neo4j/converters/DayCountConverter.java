package com.acuo.persist.neo4j.converters;

import com.opengamma.strata.basics.date.DayCount;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.Objects;

@Slf4j
public class DayCountConverter implements AttributeConverter<DayCount, String> {
    @Override
    public String toGraphProperty(DayCount value) {
        if (Objects.isNull(value)) return null;
        return value.getName();
    }

    @Override
    public DayCount toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        try
        {
            return DayCount.of(value);
        }
        catch (Exception e)
        {
            log.error("error in DayCountConverter", e);
            return null;
        }
    }
}
