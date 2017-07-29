package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class AssetValue extends Value<AssetValue> {

    private Double yield;

    private Double unitValue;

    private String priceQuotationType;

    @Convert(CurrencyConverter.class)
    private Currency nominalCurrency;

    @Convert(CurrencyConverter.class)
    private Currency reportCurrency;

    private Double coupon;

    @Relationship(type = "VALUE", direction = Relationship.INCOMING)
    private AssetValuation valuation;
}
