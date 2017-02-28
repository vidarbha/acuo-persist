package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class InitialMargin extends MarginCall {
    private Double exchangeRequirement;
    private Double brokerRequirement;
    private Double initialBalanceCash;
    private Double initialBalanceNonCash;
    private String IMRole;

}
