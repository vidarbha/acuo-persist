package com.acuo.persist.entity;

import com.acuo.common.util.ArgChecker;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.time.LocalDate;
import java.util.Set;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@NodeEntity
@Data
@ToString(callSuper = true, exclude = {"values", "latestValue"})
@EqualsAndHashCode(callSuper = true, exclude = {"values", "latestValue"})
public class TradeValuation extends Valuation {

    @Relationship(type = "VALUATED")
    private Trade trade;

    @Relationship(type = "VALUE")
    private Set<TradeValue> values;

    public boolean isValuedFor(LocalDate valuationDate) {
        ArgChecker.notNull(valuationDate, "valuationDate");
        return values.stream().anyMatch(value -> valuationDate.isEqual(value.getValuationDate()));
    }
}


