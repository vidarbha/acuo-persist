package com.acuo.persist.entity;

import com.acuo.common.ids.AssetId;
import com.acuo.common.model.assets.Assets;
import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.TypedStringConverter.AssetIdConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
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
@EqualsAndHashCode(exclude = {"id", "holds", "transfers", "settlement", "errors"})
@ToString(exclude = {"holds", "transfers", "settlement", "errors"})
public class Asset implements Entity<Asset> {

    public Asset() {
    }

    public Asset(Assets model) {
        setAssetId(AssetId.fromString(model.getAssetId()));
        setIdType(model.getIdType());
        setName(model.getName());
        setCurrency(model.getCurrency());
        //setYield();
        setType(model.getType());
        setICADCode(model.getICADCode());
        //setACUOCategory();
        setParValue(model.getParValue());
        setMinUnit(model.getMinUnit());
        setIssueDate(model.getIssueDate());
        setMaturityDate(model.getMaturityDate());
        //setTimeToMaturityDays();
        //setTimeToMaturityYears();
    }

    public Assets model() {
        Assets asset = new Assets();
        asset.setAssetId(assetId.toString());
        asset.setIdType(idType);
        asset.setName(name);
        asset.setCurrency(currency);
        asset.setType(type);
        asset.setICADCode(ICADCode);
        asset.setParValue(parValue);
        asset.setMinUnit(minUnit);
        asset.setIssueDate(issueDate);
        asset.setMaturityDate(maturityDate);
        return asset;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "id")
    @Id
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
    private Double parValue;
    private Double minUnit;
    @Convert(LocalDateConverter.class)
    private LocalDate issueDate;
    @Convert(LocalDateConverter.class)
    private LocalDate maturityDate;
    private Double timeToMaturityDays;
    private Double timeToMaturityYears;

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
