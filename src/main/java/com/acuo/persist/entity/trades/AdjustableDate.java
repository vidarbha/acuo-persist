package com.acuo.persist.entity.trades;


import com.acuo.persist.entity.Entity;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;

@NodeEntity
@Data
public class AdjustableDate implements Entity<AdjustableDate> {

    public AdjustableDate() {}

    public AdjustableDate(com.acuo.common.model.AdjustableDate model) {
        this.date = model.getDate();
        this.adjustment = new BusinessDayAdjustment(model.getAdjustment());
    }

    public com.acuo.common.model.AdjustableDate model() {
        com.acuo.common.model.AdjustableDate model = new com.acuo.common.model.AdjustableDate();
        model.setDate(date);
        model.setAdjustment(adjustment.model());
        return model;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Convert(LocalDateConverter.class)
    private LocalDate date;

    private BusinessDayAdjustment adjustment;
}
