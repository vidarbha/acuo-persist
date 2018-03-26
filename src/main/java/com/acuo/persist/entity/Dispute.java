package com.acuo.persist.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

import static com.acuo.common.model.margin.Types.DisputeReasonCode;

@NodeEntity
@Data
public class Dispute implements Entity<Dispute> {

    @Id
    @GeneratedValue
    private Long id;

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