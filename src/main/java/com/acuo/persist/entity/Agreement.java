package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalTimeConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Agreement extends Entity {

    @Property(name="id")
    private String agreementId;

    private String name;

    @Convert(LocalDateConverter.class)
    private LocalDate date;

    private String type;

    @Convert(LocalTimeConverter.class)
    private LocalTime notificationTime;

    @Convert(CurrencyConverter.class)
    private Currency currency;

    //@Relationship(type = "CLIENT_SIGNS")
    //private Set<ClientSignsRelation> clientSignsRelations;

    //@Relationship(type = "COUNTERPARTY_SIGNS", direction = Relationship.INCOMING)
    //private Set<LegalEntity> cptyEntitys;

    //@Relationship(type = "CLIENT_SIGNS")
    //private Set<LegalEntity> clients;

    //@Relationship(type = "COUNTERPARTY_SIGNS")
    //private Set<LegalEntity> counterparts;

    @Relationship(type = "STEMS_FROM", direction = Relationship.INCOMING)
    private  Set<MarginStatement> marginStatements;
}
