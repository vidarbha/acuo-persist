package com.acuo.persist.entity.trades;

import com.acuo.persist.entity.Entity;
import com.acuo.persist.neo4j.converters.BusinessDayConventionConverter;
import com.opengamma.strata.basics.date.BusinessDayConvention;
import com.opengamma.strata.basics.date.HolidayCalendarId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessDayAdjustment extends Entity<BusinessDayAdjustment> {

    public BusinessDayAdjustment() {}

    public BusinessDayAdjustment(com.acuo.common.model.BusinessDayAdjustment model) {
        this.convention = model.getConvention();
        this.holidays = model.getHolidays().stream().map(HolidayCalendarId::toString).collect(toSet());
    }

    public com.acuo.common.model.BusinessDayAdjustment model() {
        com.acuo.common.model.BusinessDayAdjustment model = new com.acuo.common.model.BusinessDayAdjustment();
        model.setConvention(convention);
        model.setHolidays(holidays.stream().map(HolidayCalendarId::of).collect(toSet()));
        return model;
    }

    @Convert(BusinessDayConventionConverter.class)
    private BusinessDayConvention convention;

    private Set<String> holidays;
}
