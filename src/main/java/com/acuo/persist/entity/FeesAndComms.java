package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class FeesAndComms extends StatementItem<FeesAndComms> {
    private Double clearingFee;
    private Double brokerFee;
}
