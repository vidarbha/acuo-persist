package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"holds", "transfer"})
@ToString(exclude = {"holds", "transfer"})
public class Asset extends Entity<Asset> {

    @Property(name = "id")
    @Index(primary = true)
    private String assetId;
    private String idType;
    private String name;
    private String isin;
    @Convert(CurrencyConverter.class)
    private Currency currency;
    private Double yield;
    private String type;
    private String ICADCode;
    private String ACUOCategory;
    private Double price;
    private Double parValue;
    private Double minUnit;
    private Double minUnitValue;
    @Convert(LocalDateConverter.class)
    private LocalDate issueDate;
    @Convert(LocalDateConverter.class)
    private LocalDate maturityDate;
    private Double timeToMaturityDays;
    private Double timeToMaturityYears;
    private String rating;
    @Convert(LocalDateConverter.class)
    private LocalDate settlementTime;

    @Relationship(type = "IS_IN")
    private AssetCategory assetCategory;

    @Relationship(type = "HOLDS", direction = Relationship.INCOMING)
    private Holds holds;

    @Relationship(type = "APPLIES_TO", direction = Relationship.INCOMING)
    private Set<Rule> rules;

    @Relationship(type = "OF", direction = Relationship.INCOMING)
    private Set<AssetTransfer> transfers;

    @Relationship(type = "PRICING_SOURCE")
    private PricingSource pricingSource;
}
