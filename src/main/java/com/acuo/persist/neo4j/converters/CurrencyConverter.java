package com.acuo.persist.neo4j.converters;

import com.opengamma.strata.basics.currency.Currency;
import org.neo4j.ogm.typeconversion.AttributeConverter;

public class CurrencyConverter implements AttributeConverter<Currency, String> {
    @Override
    public String toGraphProperty(Currency value) {
        return value.getCode();
    }

    @Override
    public Currency toEntityAttribute(String value) {
        return Currency.parse(value);
    }
}
