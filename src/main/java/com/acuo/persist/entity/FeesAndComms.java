package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class FeesAndComms extends StatementItem<FeesAndComms> {

    @Property(name="id")
    @Index(primary = true)
    private String feesAndCommsId;
    private Double clearingFee;
    private Double brokerFee;
}
