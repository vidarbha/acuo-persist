package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type="VALUE")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"value", "valuation"})
public class TradeValueRelation extends ValueRelation {

    @StartNode
    private TradeValuation valuation;

    @EndNode
    private TradeValue value;
}
