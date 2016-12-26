package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@NodeEntity
@Getter
@Setter
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

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime notificationTime;

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


    @Override
    public String toString() {
        return "MarginCall{" +
                "direction='" + direction + '\'' +
                ", callDate=" + callDate +
                ", callAmount=" + callAmount +
                ", callType='" + callType + '\'' +
                ", IMRole='" + IMRole + '\'' +
                ", valuationDate=" + valuationDate +
                ", exposure=" + exposure +
                ", pendingCollateral=" + pendingCollateral +
                ", agreementId='" + agreementId + '\'' +
                ", collateralValue=" + collateralValue +
                ", deliverAmount=" + deliverAmount +
                ", currency='" + currency + '\'' +
                ", marginCallId='" + marginCallId + '\'' +
                ", returnAmount=" + returnAmount +
                ", status='" + status + '\'' +
                ", parentRank=" + parentRank +
                ", roundedDeliverAmount=" + roundedDeliverAmount +
                ", roundedReturnAmount=" + roundedReturnAmount +
                ", belowMTA=" + belowMTA +
                ", firstStep=" + firstStep +
                ", lastStep=" + lastStep +
                '}';
    }
}
