package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MarginValuation extends Valuation<MarginValue> {
}