package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.util.Date;

@NodeEntity
public class MarginCall extends Entity{

    private String direction;

    @DateString(value = "dd/MM/yy")
    private Date callDate;

    private Double callAmount;

    private String callType;

    private String IMRole;

    @DateString(value = "dd/MM/yy")
    private Date valuationDate;

    private Double exposure;

    private Double pendingCollateral;

//    @DateString(value = "dd/MM/yy HH:mm:ss")
//    private Date notificationTime;

    private String agreementId;

    private Double collateralValue;

    private Double deliverAmount;

    private String currency;

    @Property(name="id")
    private String marginCallId;

    private Double returnAmount;

    private String status;

    private Integer parentRank;

    private Double roundedDeliverAmount;

    private Double roundedReturnAmount;

    private Integer belowMTA;


    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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

    public Double getExposure() {
        return exposure;
    }

    public void setExposure(Double exposure) {
        this.exposure = exposure;
    }

    public Double getPendingCollateral() {
        return pendingCollateral;
    }

    public void setPendingCollateral(Double pendingCollateral) {
        this.pendingCollateral = pendingCollateral;
    }

//    public Date getNotificationTime() {
//        return notificationTime;
//    }
//
//    public void setNotificationTime(Date notificationTime) {
//        this.notificationTime = notificationTime;
//    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    public Double getCollateralValue() {
        return collateralValue;
    }

    public void setCollateralValue(Double collateralValue) {
        this.collateralValue = collateralValue;
    }

    public Double getDeliverAmount() {
        return deliverAmount;
    }

    public void setDeliverAmount(Double deliverAmount) {
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

    public Double getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(Double returnAmount) {
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

    public Double getRoundedDeliverAmount() {
        return roundedDeliverAmount;
    }

    public void setRoundedDeliverAmount(Double roundedDeliverAmount) {
        this.roundedDeliverAmount = roundedDeliverAmount;
    }

    public Double getRoundedReturnAmount() {
        return roundedReturnAmount;
    }

    public void setRoundedReturnAmount(Double roundedReturnAmount) {
        this.roundedReturnAmount = roundedReturnAmount;
    }

    public Integer getBelowMTA() {
        return belowMTA;
    }

    public void setBelowMTA(Integer belowMTA) {
        this.belowMTA = belowMTA;
    }

    @Override
    public String toString() {
        return "MarginCall{" +
                "direction='" + direction + '\'' +
                ", callDate=" + callDate +
                ", callAmount=" + callAmount +
                ", callType='" + callType + '\'' +
                ", IMRole='" + IMRole + '\'' +
                ", valuationDate=" + valuationDate +
                ", exposure=" + exposure +
                ", pendingCollateral=" + pendingCollateral +
//                ", notificationTime=" + notificationTime +
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
                '}';
    }
}
