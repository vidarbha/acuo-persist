package com.acuo.persist.entity;

import com.acuo.common.model.margin.Types;
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
@EqualsAndHashCode(callSuper = false, exclude = {"marginCalls", "agreement"})
@ToString(exclude = {"marginCalls", "agreement"})
public class MarginCall extends StatementItem {

    @Property(name="id")
    private String marginCallId;

    private Double excessAmount;

    private Double balanceAmount;

    private Double deliverAmount;

    private Double returnAmount;

    private Double pendingCollateral;

    private Double exposure;




    @Relationship(type = "FIRST")
    private Step firstStep;

    @Relationship(type = "LAST")
    private Step lastStep;

    @Relationship(type = "MATCHED_TO_EXPECTED", direction = Relationship.OUTGOING)
    private MarginCall matchedToExpected;

    @Relationship(type = "CHILD_OF", direction = Relationship.INCOMING)
    private Set<ChildOf> children;

    @Relationship(type = "STEMS_FROM", direction = Relationship.OUTGOING)
    private Agreement agreement;


    public MarginCall(String marginCallId ,LocalDate callDate, Types.MarginType marginType,String direction ,LocalDate valuationDate,String currency,
            Double excessAmount, Double balanceAmount,Double deliverAmount, Double returnAmount,Double pendingCollateral,Double exposure,Integer parentRank,
                      LocalDateTime notificationTime,Double marginAmount,String status)
    {
        this.marginCallId = marginCallId;
        this.callDate = callDate;
        this.marginType = marginType;
        this.direction = direction;
        this.valuationDate = valuationDate;
        this.currency = currency;
        this.excessAmount = excessAmount;
        this.balanceAmount = balanceAmount;
        this.deliverAmount = deliverAmount;
        this.returnAmount = returnAmount;
        this.pendingCollateral = pendingCollateral;
        this.exposure = exposure;
        this.parentRank = parentRank;
        this.notificationTime = notificationTime;

        this.marginAmount = marginAmount;
        this.status = status;
    }

    public MarginCall() {}
}
