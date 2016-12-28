package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalTimeConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@NodeEntity
@Getter
@Setter
public class Agreement extends Entity<Agreement> {

    @Property(name="id")
    private String agreementId;

    private String name;

    @Convert(LocalDateConverter.class)
    @Property(name = "agreementDate")
    private LocalDate date;

    private String type;

    @Convert(LocalTimeConverter.class)
    private LocalTime notificationTime;

    @Convert(CurrencyConverter.class)
    private Currency currency;

    @Relationship(type = "CLIENT_SIGNS", direction = Relationship.INCOMING)
    private ClientSignsRelation clientSignsRelation;

    @Relationship(type = "COUNTERPARTY_SIGNS", direction = Relationship.INCOMING)
    private CounterpartSignsRelation counterpartSignsRelation;

    @Relationship(type = "COUNTERPARTY_SIGNS", direction = Relationship.INCOMING)
    private FCM fcm;

    @Relationship(type = "STEMS_FROM", direction = Relationship.INCOMING)
    private  Set<MarginStatement> marginStatements;

    @Override
    public String toString() {
        return "Agreement{" +
                "agreementId='" + agreementId + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", notificationTime=" + notificationTime +
                ", currency=" + currency +
                '}';
    }
}
