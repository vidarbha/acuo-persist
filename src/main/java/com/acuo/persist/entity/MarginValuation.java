package com.acuo.persist.entity;

import com.acuo.common.model.margin.Types;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
@Data
@ToString(callSuper = true, exclude = {"values"})
@EqualsAndHashCode(callSuper = true, exclude = {"values"})
public class MarginValuation extends Valuation {

    private Types.CallType callType;

    @Relationship(type = "VALUATED")
    private Portfolio portfolio;

    @Relationship(type = "VALUE")
    private Set<MarginValue> values;
}