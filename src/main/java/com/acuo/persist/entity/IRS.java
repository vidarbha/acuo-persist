package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.util.Date;
import java.util.Set;

@NodeEntity
public class IRS extends Entity{

    @DateString(value = "dd/MM/yy")
    private Date maturity;

    @DateString(value = "dd/MM/yy")
    private Date clearingDate;

    @Property(name="id")
    private String irsId;

    @Relationship(type = "PAYS")
    private Set<Leg> payLegs;

    @Relationship(type = "RECEIVE")
    private Set<Leg> receiveLegs;

    private String tradeType;

    private Account account;

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
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

    public String getIrsId() {
        return irsId;
    }

    public void setIrsId(String irsId) {
        this.irsId = irsId;
    }

    @Override
    public String toString() {
        return "IRS{" +
                "maturity=" + maturity +
                ", clearingDate=" + clearingDate +
                ", irsId='" + irsId + '\'' +
                ", payLegs=" + payLegs +
                ", receiveLegs=" + receiveLegs +
                ", tradeType='" + tradeType + '\'' +
                ", account=" + account +
                '}';
    }
}
