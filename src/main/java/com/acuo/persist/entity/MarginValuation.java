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
@ToString(callSuper = true, exclude = {"latestValue","values"})
@EqualsAndHashCode(callSuper = true, exclude = {"latestValue","values"})
public class MarginValuation extends Valuation<MarginValue> {

    private Types.CallType callType;

    @Relationship(type = "VALUATED")
    private Portfolio portfolio;

    @Relationship(type = "LATEST")
    protected MarginValue latestValue;

    @Relationship(type = "VALUE")
    protected Set<MarginValue> values;
}