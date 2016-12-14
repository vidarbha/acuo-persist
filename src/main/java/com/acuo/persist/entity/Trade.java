package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Trade extends Entity{

    private String underlyingAssetId;

    private Double notional;

    private  String buySellProtection;

    private Double couponRate;

    @Convert(LocalDateConverter.class)
    private LocalDate maturity;

    @Convert(LocalDateConverter.class)
    private LocalDate clearingDate;

    @Convert(CurrencyConverter.class)
    private Currency currency;

    @Property(name="id")
    private String tradeId;

    private String underlyingEntity;

    private Double factor;

    private String seniority;

    @Relationship(type = "VALUATED")
    private Set<Valuation> valuations = new HashSet<>();

}
