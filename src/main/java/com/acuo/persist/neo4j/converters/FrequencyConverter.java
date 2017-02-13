package com.acuo.persist.neo4j.converters;

import com.opengamma.strata.basics.schedule.Frequency;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.Objects;
@Slf4j
public class FrequencyConverter implements AttributeConverter<Frequency, String> {
    @Override
    public String toGraphProperty(Frequency value) {
        if (Objects.isNull(value)) return null;
        return value.toString();
    }

    @Override
    public Frequency toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        try
        {
            return Frequency.parse(value);
        }
        catch (Exception e)
        {
            log.error("error in FrequencyConverter", e);
            return  null;
        }

    }
}
