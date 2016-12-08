package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.math.BigDecimal;
import java.util.Date;

@NodeEntity
public class MarginCall extends Entity{

    @DateString(value = "dd/MM/yy")
    private Date callDate;

    private Double callAmount;

    private String callType;

    private String IMRole;

    @DateString(value = "dd/MM/yy")
    private Date valuationDate;

    private BigDecimal exposure;

    private Double pendingCollateral;

    @DateString(value = "dd/MM/yy hh:mm:ss")
    private Date notificationTime;

    private String agreementId;

    private BigDecimal collateralValue;

    private BigDecimal deliverAmount;

    private String currency;

    @Property(name="id")
    private String marginCallId;

    private BigDecimal returnAmount;

    private String status;

    private Integer parentRank;

    private BigDecimal roundedDeliverAmount;

    private BigDecimal roundedReturnAmount;

    private Integer belowMTA;

    @Relationship(type = "PART_OF")
    private MarginStatement marginStatement;

    @Relationship(type = "STEMS_FROM")
    private Agreement agreement;

    @Relationship(type = "DIRECTED_TO")
    private Client client;

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getCallDate() {
        return callDate;
    }

    public void setCallDate(Date callDate) {
        this.callDate = callDate;
    }

    public Double getCallAmount() {
        return callAmount;
    }

    public void setCallAmount(Double callAmount) {
        this.callAmount = callAmount;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getIMRole() {
        return IMRole;
    }

    public void setIMRole(String IMRole) {
        this.IMRole = IMRole;
    }

    public Date getValuationDate() {
        return valuationDate;
    }

    public void setValuationDate(Date valuationDate) {
        this.valuationDate = valuationDate;
    }

    public BigDecimal getExposure() {
        return exposure;
    }

    public void setExposure(BigDecimal exposure) {
        this.exposure = exposure;
    }

    public Double getPendingCollateral() {
        return pendingCollateral;
    }

    public void setPendingCollateral(Double pendingCollateral) {
        this.pendingCollateral = pendingCollateral;
    }

    public Date getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Date notificationTime) {
        this.notificationTime = notificationTime;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public BigDecimal getCollateralValue() {
        return collateralValue;
    }

    public void setCollateralValue(BigDecimal collateralValue) {
        this.collateralValue = collateralValue;
    }

    public BigDecimal getDeliverAmount() {
        return deliverAmount;
    }

    public void setDeliverAmount(BigDecimal deliverAmount) {
        this.deliverAmount = deliverAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMarginCallId() {
        return marginCallId;
    }

    public void setMarginCallId(String marginCallId) {
        this.marginCallId = marginCallId;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getParentRank() {
        return parentRank;
    }

    public void setParentRank(Integer parentRank) {
        this.parentRank = parentRank;
    }

    public BigDecimal getRoundedDeliverAmount() {
        return roundedDeliverAmount;
    }

    public void setRoundedDeliverAmount(BigDecimal roundedDeliverAmount) {
        this.roundedDeliverAmount = roundedDeliverAmount;
    }

    public BigDecimal getRoundedReturnAmount() {
        return roundedReturnAmount;
    }

    public void setRoundedReturnAmount(BigDecimal roundedReturnAmount) {
        this.roundedReturnAmount = roundedReturnAmount;
    }

    public Integer getBelowMTA() {
        return belowMTA;
    }

    public void setBelowMTA(Integer belowMTA) {
        this.belowMTA = belowMTA;
    }

    public MarginStatement getMarginStatement() {
        return marginStatement;
    }

    public void setMarginStatement(MarginStatement marginStatement) {
        this.marginStatement = marginStatement;
    }

    @Override
    public String toString() {
        return "MarginCall{" +
                "callDate=" + callDate +
                ", callAmount=" + callAmount +
                ", callType='" + callType + '\'' +
                ", IMRole='" + IMRole + '\'' +
                ", valuationDate=" + valuationDate +
                ", exposure=" + exposure +
                ", pendingCollateral=" + pendingCollateral +
                ", notificationTime=" + notificationTime +
                ", agreementId='" + agreementId + '\'' +
                ", collateralValue=" + collateralValue +
                ", deliverAmount=" + deliverAmount +
                ", currency='" + currency + '\'' +
                ", marginCallId='" + marginCallId + '\'' +
                ", returnAmount=" + returnAmount +
                ", status='" + status + '\'' +
                ", parentRank=" + parentRank +
                ", roundedDeliverAmount=" + roundedDeliverAmount +
                ", roundedReturnAmount=" + roundedReturnAmount +
                ", belowMTA=" + belowMTA +
                ", marginStatement=" + marginStatement +
                '}';
    }
}
