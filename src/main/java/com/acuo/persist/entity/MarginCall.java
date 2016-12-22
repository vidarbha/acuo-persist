package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class MarginCall extends Entity{

    private String direction;

    @Convert(LocalDateConverter.class)
    private LocalDate callDate;

    private Double callAmount;

    private String callType;

    private String IMRole;

    @Convert(LocalDateConverter.class)
    private LocalDate valuationDate;

    private Double exposure;

    private Double pendingCollateral;

//    @DateString(value = "dd/MM/yy HH:mm:ss")
//    private Date notificationTime;

    private String agreementId;

    private Double collateralValue;

    private Double deliverAmount;

    private String currency;

    @Property(name="id")
    private String marginCallId;

    private Double returnAmount;

    private String status;

    private Integer parentRank;

    private Double roundedDeliverAmount;

    private Double roundedReturnAmount;

    private Integer belowMTA;

    @Relationship(type = "FIRST", direction = Relationship.OUTGOING)
    private Step firstStep;

    @Relationship(type = "LAST", direction = Relationship.OUTGOING)
    private Step lastStep;

    @Relationship(type = "CHILD_OF", direction = Relationship.INCOMING)
    private Set<MarginCall> marginCalls;


}
