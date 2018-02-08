package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NodeEntity
@Data
@EqualsAndHashCode(exclude = {"id", "queryDateTime", "settlement"})
@ToString(exclude = {"settlement"})
public class SettlementDate implements Entity<SettlementDate>{

    @Id
    @GeneratedValue
    private Long id;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime queryDateTime;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime creationDateTime;

    @Convert(LocalDateConverter.class)
    private LocalDate settlementDate;

    @Relationship(type = "SETTLEMENT_DATE", direction = Relationship.INCOMING)
    private Settlement settlement;
}
