package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

import static com.acuo.common.model.margin.Types.DisputeReasonCode;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Dispute extends Entity<Dispute> {

    @Relationship(type = "ON")
    private MarginStatement marginStatement;

    private Set<DisputeReasonCode> disputeReasonCodes;
    private String comments;
    private Double disputedAmount;
    private Double agreedAmount;
    private Double mtm;
    private Double balance;
    private Double im;

}