package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalTimeConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"clientSignsRelation", "counterpartSignsRelation", "marginStatements", "marginCalls"})
@ToString(exclude = {"clientSignsRelation", "counterpartSignsRelation", "marginStatements", "marginCalls"})
public class Agreement extends Entity<Agreement> {

    @Property(name="id")
    private String agreementId;

    private String ampId;

    private String name;

    @Convert(LocalDateConverter.class)
    @Property(name = "agreementDate")
    private LocalDate date;

    private String type;

    @Convert(LocalTimeConverter.class)
    private LocalTime notificationTime;

    @Convert(CurrencyConverter.class)
    private Currency currency;

    private String FCMCustodian;

    private Double tolerance;

    private String interestTransfer;

    private String interestPaymentNetting;

    private String interestAdjustment;

    private String negativeInterest;

    private String dailyInterestCompounding;

    @Relationship(type = "CLIENT_SIGNS", direction = Relationship.INCOMING)
    private ClientSignsRelation clientSignsRelation;

    @Relationship(type = "COUNTERPARTY_SIGNS", direction = Relationship.INCOMING)
    private CounterpartSignsRelation counterpartSignsRelation;

    /*@Relationship(type = "STEMS_FROM", direction = Relationship.INCOMING)
    private  Set<MarginStatement> marginStatements;*/


}