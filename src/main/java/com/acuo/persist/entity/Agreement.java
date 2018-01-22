package com.acuo.persist.entity;

import com.acuo.common.ids.ClientId;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalTimeConverter;
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
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static com.acuo.common.util.ArgChecker.notNull;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"clientSignsRelation", "counterpartSignsRelation"})
@ToString(exclude = {"clientSignsRelation", "counterpartSignsRelation"})
public class Agreement extends Entity<Agreement> {

    public Agreement() {
    }

    public Agreement(com.acuo.common.model.agreements.Agreement model) {
        setAgreementId(model.getId());
        setAmpId(model.getAmpId());
        setName(model.getLongName()); //TODO: to check
        //setDate(); //TODO: to many dates on the model
        setType(model.getAgreementType().name());
        //setLaw();
        //setNotificationTime();
        //setCurrency(); //TODO: to many currencies on the model
        //setFCMCustodian();
        //setCounterpartCustodian();
        //setTolerance();
        setThreshold(model.getThreshold());
        //setInterestTransfer();
        //setInterestPaymentNetting();
        //setInterestAdjustment();
        //setNegativeInterest();
        //setDailyInterestCompounding();
    }

    public com.acuo.common.model.agreements.Agreement model() {
        com.acuo.common.model.agreements.Agreement model = new com.acuo.common.model.agreements.Agreement();
        model.setId(agreementId);
        model.setAmpId(ampId);
        model.setLongName(name);
        model.setAgreementType(Types.AgreementType.valueOf(type));
        model.setThreshold(threshold);
        return model;
    }

    @Property(name="id")
    @Index(primary = true)
    private String agreementId;

    private String ampId;

    private String name;

    @Convert(LocalDateConverter.class)
    @Property(name = "agreementDate")
    private LocalDate date;

    private String type;

    private String law;

    @Convert(LocalTimeConverter.class)
    private LocalTime notificationTime;

    @Convert(CurrencyConverter.class)
    private Currency currency;

    private String FCMCustodian;

    private String counterpartCustodian;

    private Double tolerance;

    private Double threshold;

    private String interestTransfer;

    private String interestPaymentNetting;

    private String interestAdjustment;

    private String negativeInterest;

    private String dailyInterestCompounding;

    @Relationship(type = "CLIENT_SIGNS", direction = Relationship.INCOMING)
    private ClientSignsRelation clientSignsRelation;

    @Relationship(type = "COUNTERPARTY_SIGNS", direction = Relationship.INCOMING)
    private CounterpartSignsRelation counterpartSignsRelation;

    @Relationship(type = "IS_COMPOSED_OF")
    private Set<Rule> rules = new HashSet<>();

    public LegalEntity clientEntity() {
        final ClientSignsRelation relation = notNull(getClientSignsRelation(), "ClientSignsRelation");
        final LegalEntity entity = relation.getLegalEntity();
        return notNull(entity, "LegalEntity");
    }

    public LegalEntity counterpartEntity() {
        final CounterpartSignsRelation relation = notNull(getCounterpartSignsRelation(), "CounterpartSignsRelation");
        final LegalEntity entity = relation.getLegalEntity();
        return notNull(entity, "LegalEntity");
    }

    public ClientId clientId() {
        LegalEntity legalEntity = clientEntity();
        final Firm firm = notNull(legalEntity.getFirm(), "Firm");
        return ClientId.fromString(firm.getFirmId());
    }
}