package com.acuo.persist.neo4j.converters;

import com.opengamma.strata.basics.index.FloatingRateName;
import com.opengamma.strata.basics.index.FloatingRateType;
import com.opengamma.strata.basics.index.ImmutableFloatingRateName;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.Objects;

@Slf4j
public class FloatingRateNameConverter implements AttributeConverter<FloatingRateName, String> {
    @Override
    public String toGraphProperty(FloatingRateName value) {
        if (Objects.isNull(value)) return null;
        return value.getName();
    }

    @Override
    public FloatingRateName toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        try{
            return FloatingRateName.of(value);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ImmutableFloatingRateName.of(value,value, FloatingRateType.IBOR);
        }
    }
}
