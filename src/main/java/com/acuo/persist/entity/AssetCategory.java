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
@EqualsAndHashCode(callSuper = false)
public class AssetCategory extends Entity<AssetCategory> {

    private String name;
    private String type;
    @Convert(CurrencyConverter.class)
    private Currency currency;
    private String ticker;
    private Double maturityLb;
    private Double maturityUp;

    @Relationship(type = "IS_ELIGIBLE_UNDER", direction = Relationship.OUTGOING)
    private EligibleUnder eligibleUnder;

}
