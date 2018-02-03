package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"asset"})
@ToString(exclude = {"asset"})
public class Settlement extends Entity<Settlement> {

    @Property(name = "id")
    @Index(unique = true)
    private String settlementId;

    @Relationship(type = "SETTLEMENT", direction = Relationship.INCOMING)
    private Asset asset;

    @Relationship(type = "LATEST")
    private SettlementDate latestSettlementDate;

    @Relationship(type = "SETTLEMENT_DATE")
    private Set<SettlementDate> settlementDates;
}
