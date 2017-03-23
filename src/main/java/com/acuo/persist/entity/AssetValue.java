package com.acuo.persist.entity;

import com.opengamma.strata.basics.currency.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;

import java.time.LocalDate;
import java.util.*;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class AssetValue extends Value {

    private Double yield;
    private Double price;
    private LocalDate valuationDateTime;
    private String priceQuotationType;
    private Currency nominalCurrency;
    private Currency reportCurrency;
    private Double coupon;



}
