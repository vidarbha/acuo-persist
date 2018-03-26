package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type="COUNTERPARTY_SIGNS")
@Data
@EqualsAndHashCode(exclude = {"legalEntity", "agreement"})
@ToString(exclude = {"legalEntity", "agreement"})
public class CounterpartSignsRelation implements Entity<CounterpartSignsRelation> {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private LegalEntity legalEntity;

    @EndNode
    private Agreement agreement;

    private Double rounding;

    private Double MTA;

}
