package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;

@RelationshipEntity(type="VALUE")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"value"})
public class TradeValueRelation extends ValueRelation {

    @EndNode
    private TradeValue value;
}
