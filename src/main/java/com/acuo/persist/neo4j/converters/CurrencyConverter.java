package com.acuo.persist.neo4j.converters;

import com.opengamma.strata.basics.currency.Currency;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.Objects;

public class CurrencyConverter implements AttributeConverter<Currency, String> {
    @Override
    public String toGraphProperty(Currency value) {
        if (Objects.isNull(value)) return null;
        return value.getCode();
    }

    @Override
    public Currency toEntityAttribute(String value) {
        if (Objects.isNull(value)) return null;
        return Currency.parse(value);
    }
}
