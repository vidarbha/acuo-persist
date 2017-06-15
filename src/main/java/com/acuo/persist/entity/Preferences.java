package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "PREFERENCES")
@Data
@EqualsAndHashCode(callSuper = true)
public class Preferences extends Entity<Preferences> {

    private Double tolerance;

    @StartNode
    private LegalEntity start;

    @EndNode
    private LegalEntity end;
}
