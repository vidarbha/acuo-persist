package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.*;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"marginCalls"})
@ToString(exclude = {"marginCalls"})
public class MarginCall extends Entity<MarginCall> {

    @Property(name="id")
    private String marginCallId;

    @Convert(LocalDateConverter.class)
    private LocalDate callDate;

    private String callType;

    private String direction;

    @Convert(LocalDateConverter.class)
    private LocalDate valuationDate;

    private String currency;

    private Double excessAmount;

    private Double balanceAmount;

    private Double deliverAmount;

    private Double returnAmount;

    private Double pendingCollateral;

    private Double exposure;

    private String IMRole;

    private Integer parentRank;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime notificationTime;

    private Double roundedReturnAmount;

    private Double roundedDeliverAmount;

    private Integer belowMTA;

    @Relationship(type = "FIRST")
    private Step firstStep;

    @Relationship(type = "LAST")
    private Step lastStep;

    @Relationship(type = "CHILD_OF", direction = Relationship.INCOMING)
    private Set<MarginCall> marginCalls;
}
