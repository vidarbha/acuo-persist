package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.opengamma.strata.basics.currency.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.util.*;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class AssetValue extends Value {

    private Double yield;

    private Double price;

    @Convert(LocalDateConverter.class)
    private LocalDate valuationDateTime;

    private String priceQuotationType;

    @Convert(CurrencyConverter.class)
    private Currency nominalCurrency;

    @Convert(CurrencyConverter.class)
    private Currency reportCurrency;

    private Double coupon;



}
