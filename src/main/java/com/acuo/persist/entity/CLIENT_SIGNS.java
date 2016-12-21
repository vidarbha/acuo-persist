package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type="CLIENT_SIGNS")
public class CLIENT_SIGNS extends Entity{

    @StartNode
    private LegalEntity legalEntity;

    @EndNode
    private Agreement agreement;

    private Double initialMarginBalance;

    private Double rounding;

    private Double MTA;

    private Double variationMarginBalance;

    public LegalEntity getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(LegalEntity legalEntity) {
        this.legalEntity = legalEntity;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public Double getInitialMarginBalance() {
        return initialMarginBalance;
    }

    public void setInitialMarginBalance(Double initialMarginBalance) {
        this.initialMarginBalance = initialMarginBalance;
    }

    public Double getRounding() {
        return rounding;
    }

    public void setRounding(Double rounding) {
        this.rounding = rounding;
    }

    public Double getMTA() {
        return MTA;
    }

    public void setMTA(Double MTA) {
        this.MTA = MTA;
    }

    public Double getVariationMarginBalance() {
        return variationMarginBalance;
    }

    public void setVariationMarginBalance(Double variationMarginBalance) {
        this.variationMarginBalance = variationMarginBalance;
    }

    @Override
    public String toString() {
        return "CLIENT_SIGNS{" +
                ", agreement=" + agreement +
                ", initialMarginBalance=" + initialMarginBalance +
                ", rounding=" + rounding +
                ", MTA=" + MTA +
                ", variationMarginBalance=" + variationMarginBalance +
                '}';
    }
}
