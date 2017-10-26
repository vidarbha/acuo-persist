package com.acuo.persist.entity;

import com.acuo.common.ids.AssetId;
import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.TypedStringConverter.AssetIdConverter;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"holds", "transfers", "settlement", "errors"})
@ToString(exclude = {"holds", "transfers", "settlement", "errors"})
public class Asset extends Entity<Asset> {

    @Property(name = "id")
    @Index(primary = true)
    @Convert(AssetIdConverter.class)
    private AssetId assetId;

    private String idType;
    private String name;
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
    private String settlementTime;

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

    @Relationship(type = "SETTLEMENT")
    private Settlement settlement;

    @Relationship(type = "ENCOUNTERS")
    private List<ServiceError> errors = new ArrayList<>();

    public void addErrors(ServiceError error) {
        errors.add(error);
    }

    public void addAllErrors(List<ServiceError> error) {
        errors.addAll(error);
    }

}
