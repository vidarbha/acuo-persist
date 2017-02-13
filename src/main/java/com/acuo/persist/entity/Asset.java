package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"possesses","holds", "earmarkedMarginCall"})
@ToString(exclude = {"possesses","holds", "earmarkedMarginCall"})
public class Asset extends Entity<Asset> {

    @Property(name = "id")
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

    @Relationship(type = "IS_IN")
    private AssetCategory assetCategory;

    @Relationship(type = "HOLDS", direction = Relationship.INCOMING)
    private Holds holds;

    @Relationship(type = "EXCLUDED")
    private MarginCall earmarkedMarginCall;

    @Relationship(type = "OF", direction = Relationship.INCOMING)
    private AssetTransfer assetTransfer;

    @Relationship(type = "IS_AVAILABLE_FOR")
    private AvailableFor availableFor;
}
