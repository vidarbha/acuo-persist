package com.acuo.persist.entity;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "PREFERENCES")
@Getter
@Setter
public class Preferences extends Entity<Preferences> {

    private Double tolerance;

    @StartNode
    private LegalEntity start;

    @EndNode
    private LegalEntity end;
}
