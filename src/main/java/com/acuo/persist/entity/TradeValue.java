package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.opengamma.strata.basics.currency.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class TradeValue extends Value<TradeValue>{

    private Double pv;

    @Convert(CurrencyConverter.class)
    private com.opengamma.strata.basics.currency.Currency currency;

    private String source;
}