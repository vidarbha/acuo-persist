package com.acuo.persist.entity;

import com.acuo.common.model.margin.Types;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class InitialMargin extends MarginCall<InitialMargin> {
    private Double exchangeRequirement;
    private Double brokerRequirement;
    private Double initialBalanceCash;
    private Double initialBalanceNonCash;
    private String IMRole;

    public InitialMargin(String marginCallId , LocalDate callDate, Types.MarginType marginType, String direction , LocalDate valuationDate, String currency,
                         Double excessAmount, Double balanceAmount, Double deliverAmount, Double returnAmount, Double pendingCollateral, Double exposure, Integer parentRank,
                         LocalDateTime notificationTime, Double marginAmount, String status)
    {
        super(marginCallId, callDate,marginType,direction,valuationDate,currency,
                excessAmount,balanceAmount, deliverAmount, returnAmount, pendingCollateral, exposure, parentRank,
                notificationTime,marginAmount,status);
    }
}
