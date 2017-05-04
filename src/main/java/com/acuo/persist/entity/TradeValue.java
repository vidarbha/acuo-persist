package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"valuation"})
public class TradeValue extends Value<TradeValue>{

    private Double pv;

    @Convert(CurrencyConverter.class)
    private Currency currency;

    @Relationship(type = "VALUE", direction = INCOMING)
    private TradeValueRelation valuation;
}
