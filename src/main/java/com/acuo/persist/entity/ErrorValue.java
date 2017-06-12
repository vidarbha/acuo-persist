package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class ErrorValue extends Value<ErrorValue> {

    private String reason;

    private String message;

    @Relationship(type = "VALUE", direction = INCOMING)
    private TradeValuation valuation;
}