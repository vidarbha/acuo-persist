package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class SettlementDate extends Entity<SettlementDate>{

    @Property(name = "id")
    @Index(primary = true)
    private String settlementDateId;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime queryDateTime;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime creationDateTime;

    @Convert(LocalDateConverter.class)
    private LocalDate settlementDate;
}
