package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.util.Date;
import java.util.Set;

@NodeEntity
public class Trade extends Entity{

    private String underlyingAssetId;

    private Double notional;

    private  String buySellProtection;

    private Double couponRate;

    @DateString(value = "dd/MM/yy")
    private Date maturity;

    @DateString(value = "dd/MM/yy")
    private Date clearingDate;

    private String currency;

    @Property(name="id")
    private String tradeId;

    private String underlyingEntity;

    private Double factor;

    private String seniority;

    @Relationship(type = "VALUATED")
    private Set<Valuation> valuations;

    @Override
    public String toString() {
        return "Trade{" +
                "underlyingAssetId='" + underlyingAssetId + '\'' +
                ", notional=" + notional +
                ", buySellProtection='" + buySellProtection + '\'' +
                ", couponRate=" + couponRate +
                ", maturity=" + maturity +
                ", clearingDate=" + clearingDate +
                ", currency='" + currency + '\'' +
                ", tradeId='" + tradeId + '\'' +
                ", underlyingEntity='" + underlyingEntity + '\'' +
                ", factor=" + factor +
                ", seniority='" + seniority + '\'' +
                ", valuations=" + valuations +
                '}';
    }

    public String getUnderlyingAssetId() {
        return underlyingAssetId;
    }

    public void setUnderlyingAssetId(String underlyingAssetId) {
        this.underlyingAssetId = underlyingAssetId;
    }

    public Double getNotional() {
        return notional;
    }

    public void setNotional(Double notional) {
        this.notional = notional;
    }

    public String getBuySellProtection() {
        return buySellProtection;
    }

    public void setBuySellProtection(String buySellProtection) {
        this.buySellProtection = buySellProtection;
    }

    public Double getCouponRate() {
        return couponRate;
    }

    public void setCouponRate(Double couponRate) {
        this.couponRate = couponRate;
    }

    public Date getMaturity() {
        return maturity;
    }

    public void setMaturity(Date maturity) {
        this.maturity = maturity;
    }

    public Date getClearingDate() {
        return clearingDate;
    }

    public void setClearingDate(Date clearingDate) {
        this.clearingDate = clearingDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getUnderlyingEntity() {
        return underlyingEntity;
    }

    public void setUnderlyingEntity(String underlyingEntity) {
        this.underlyingEntity = underlyingEntity;
    }

    public Double getFactor() {
        return factor;
    }

    public void setFactor(Double factor) {
        this.factor = factor;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    public Set<Valuation> getValuations() {
        return valuations;
    }

    public void setValuations(Set<Valuation> valuations) {
        this.valuations = valuations;
    }
}
