package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"id","asset"})
@ToString(exclude = {"asset"})
public class Settlement implements Entity<Settlement> {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "id")
    @Id
    private String settlementId;

    @Relationship(type = "SETTLEMENT", direction = Relationship.INCOMING)
    private Asset asset;

    @Relationship(type = "LATEST")
    private SettlementDate latestSettlementDate;

    @Relationship(type = "SETTLEMENT_DATE")
    private Set<SettlementDate> settlementDates;
}
