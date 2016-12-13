package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.util.Date;
import java.util.Set;

@NodeEntity
public class FRA extends Trade{

    private String legPay;

    private Double notional;

    @DateString(value = "dd/MM/yy")
    private Date maturity;

    private Double fixedRate;

    private String indexTenor;

    @DateString(value = "dd/MM/yy")
    private Date clearingDate;

    private String index;

    private String currency;

    @Property(name="id")
    private String fraId;

    @Relationship(type = "PAYS")
    private Set<Leg> payLegs;

    @Relationship(type = "RECEIVE")
    private Set<Leg> receiveLegs;

    private String tradeType;

    private Account account;

    public String getLegPay() {
        return legPay;
    }

    public void setLegPay(String legPay) {
        this.legPay = legPay;
    }

    public Double getNotional() {
        return notional;
    }

    public void setNotional(Double notional) {
        this.notional = notional;
    }

    public Date getMaturity() {
        return maturity;
    }

    public void setMaturity(Date maturity) {
        this.maturity = maturity;
    }

    public Double getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(Double fixedRate) {
        this.fixedRate = fixedRate;
    }

    public String getIndexTenor() {
        return indexTenor;
    }

    public void setIndexTenor(String indexTenor) {
        this.indexTenor = indexTenor;
    }

    public Date getClearingDate() {
        return clearingDate;
    }

    public void setClearingDate(Date clearingDate) {
        this.clearingDate = clearingDate;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFraId() {
        return fraId;
    }

    public void setFraId(String fraId) {
        this.fraId = fraId;
    }

    public Set<Leg> getPayLegs() {
        return payLegs;
    }

    public void setPayLegs(Set<Leg> payLegs) {
        this.payLegs = payLegs;
    }

    public Set<Leg> getReceiveLegs() {
        return receiveLegs;
    }

    public void setReceiveLegs(Set<Leg> receiveLegs) {
        this.receiveLegs = receiveLegs;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    @Override
    public String toString() {
        return "FRA{" +
                "legPay='" + legPay + '\'' +
                ", notional=" + notional +
                ", maturity=" + maturity +
                ", fixedRate=" + fixedRate +
                ", indexTenor='" + indexTenor + '\'' +
                ", clearingDate=" + clearingDate +
                ", index='" + index + '\'' +
                ", currency='" + currency + '\'' +
                ", fraId='" + fraId + '\'' +
                ", payLegs=" + payLegs +
                ", receiveLegs=" + receiveLegs +
                ", tradeType='" + tradeType + '\'' +
                ", account=" + account +
                '}';
    }
}
