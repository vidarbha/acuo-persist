package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

@NodeEntity
@Data
@EqualsAndHashCode(exclude = {"eligibleUnder"})
@ToString(exclude = {"eligibleUnder"})
public class AssetCategory implements Entity<AssetCategory> {

    @Id
    @GeneratedValue
    private Long id;

    private String ICADCode;

    private String type;

    @Convert(CurrencyConverter.class)
    private Currency currency;

    private String ticker;

    private Double maturityLb;

    private Double maturityUp;

    @Relationship(type = "IS_ELIGIBLE_UNDER")
    private EligibleUnder eligibleUnder;

}