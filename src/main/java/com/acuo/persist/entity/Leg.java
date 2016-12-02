package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.util.Date;

@NodeEntity
public class Leg extends Entity{

    private Double notional;

    private String dayCount;

    private String businessDayConvention;

    private Double fixedRate;

    @DateString(value = "dd/MM/yy")
    private Date payStart;

    @Property(name="id")
    private String legId;

    private String paymentFrequency;

    private String type;

    @DateString(value = "dd/MM/yy")
    private Date nextCouponPaymentDate;

    @DateString(value = "dd/MM/yy")
    private Date payEnd;

    private String refCalendar;

    private String indexTenor;

    private String index;

    private String resetFrequency;

    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getResetFrequency() {
        return resetFrequency;
    }

    public void setResetFrequency(String resetFrequency) {
        this.resetFrequency = resetFrequency;
    }

    public Double getNotional() {
        return notional;
    }

    public void setNotional(Double notional) {
        this.notional = notional;
    }

    public String getDayCount() {
        return dayCount;
    }

    public void setDayCount(String dayCount) {
        this.dayCount = dayCount;
    }

    public String getBusinessDayConvention() {
        return businessDayConvention;
    }

    public void setBusinessDayConvention(String businessDayConvention) {
        this.businessDayConvention = businessDayConvention;
    }

    public Double getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(Double fixedRate) {
        this.fixedRate = fixedRate;
    }

    public Date getPayStart() {
        return payStart;
    }

    public void setPayStart(Date payStart) {
        this.payStart = payStart;
    }

    public String getLegId() {
        return legId;
    }

    public void setLegId(String legId) {
        this.legId = legId;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getNextCouponPaymentDate() {
        return nextCouponPaymentDate;
    }

    public void setNextCouponPaymentDate(Date nextCouponPaymentDate) {
        this.nextCouponPaymentDate = nextCouponPaymentDate;
    }

    public Date getPayEnd() {
        return payEnd;
    }

    public void setPayEnd(Date payEnd) {
        this.payEnd = payEnd;
    }

    public String getRefCalendar() {
        return refCalendar;
    }

    public void setRefCalendar(String refCalendar) {
        this.refCalendar = refCalendar;
    }

    public String getIndexTenor() {
        return indexTenor;
    }

    public void setIndexTenor(String indexTenor) {
        this.indexTenor = indexTenor;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Leg{" +
                "notional=" + notional +
                ", dayCount='" + dayCount + '\'' +
                ", businessDayConvention='" + businessDayConvention + '\'' +
                ", fixedRate=" + fixedRate +
                ", payStart=" + payStart +
                ", legId='" + legId + '\'' +
                ", paymentFrequency='" + paymentFrequency + '\'' +
                ", type='" + type + '\'' +
                ", nextCouponPaymentDate=" + nextCouponPaymentDate +
                ", payEnd=" + payEnd +
                ", refCalendar='" + refCalendar + '\'' +
                ", indexTenor='" + indexTenor + '\'' +
                ", index='" + index + '\'' +
                ", resetFrequency='" + resetFrequency + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
