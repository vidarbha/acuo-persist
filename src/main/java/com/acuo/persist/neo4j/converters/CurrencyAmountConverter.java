package com.acuo.persist.neo4j.converters;

import com.opengamma.strata.basics.currency.CurrencyAmount;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.Objects;

public class CurrencyAmountConverter implements AttributeConverter<CurrencyAmount, String> {
    @Override
    public String toGraphProperty(CurrencyAmount value) {
        if (Objects.isNull(value)) return null;
        return value.toString();
    }

    @Override
    public CurrencyAmount toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        return CurrencyAmount.parse(value);
    }
}
