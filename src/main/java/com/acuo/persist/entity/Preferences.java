package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "PREFERENCES")
@Data
@EqualsAndHashCode(exclude = "id")
public class Preferences implements Entity<Preferences> {

    @Id
    @GeneratedValue
    private Long id;

    private Double tolerance;

    @StartNode
    private LegalEntity start;

    @EndNode
    private LegalEntity end;
}
