package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.causalclustering.core.consensus.outcome.Outcome;
import org.neo4j.cypher.internal.frontend.v3_1.SemanticDirection;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"settlementDates"})
@ToString(exclude = {"settlementDates"})
public class SettlementDate extends Entity<SettlementDate>{

    @Property(name = "id")
    @Index(primary = true)
    private String settlementDateId;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime queryDateTime;

    @Convert(LocalDateConverter.class)
    private LocalDate settlementDate;

    @Relationship(type = "SETTLEMENT_DATE", direction = Relationship.OUTGOING)
    private Set<SettlementDate> settlementDates;
}
