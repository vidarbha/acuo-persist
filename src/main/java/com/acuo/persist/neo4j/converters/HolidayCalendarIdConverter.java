package com.acuo.persist.neo4j.converters;

import com.opengamma.strata.basics.date.HolidayCalendarId;
import org.neo4j.ogm.typeconversion.AttributeConverter;

public class HolidayCalendarIdConverter implements AttributeConverter<HolidayCalendarId, String> {
    @Override
    public String toGraphProperty(HolidayCalendarId value) {
        return value.getName();
    }

    @Override
    public HolidayCalendarId toEntityAttribute(String value) {
        return HolidayCalendarId.of(value);
    }
}
