package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.util.Date;
import java.util.Set;

@NodeEntity
public class MarginStatement extends Entity{



    private Double totalAmount;

    private String legalEntityId;

    private String direction;

    @DateString(value = "dd/MM/yy HH:mm:ss")
    private Date date;

    private Double interestPayment;

    private Double productCashFlows;

    private Double feesCommissions;

    private Double pendingCash;

    private Double PAI;

    private Double pendingNonCash;

    @Property(name="id")
    private String marginStatementId;

    private String status;

    @Relationship(type = "STEMS_FROM")
    private Agreement agreement;

    @Relationship(type = "IS_RECEIVED_IN", direction = Relationship.INCOMING)
    private Set<MarginCall> receviedMarginCalls;

    @Relationship(type = "IS_EXPECTED_IN", direction = Relationship.INCOMING)
    private Set<MarginCall> expectedMarginCalls;

    public Set<MarginCall> getExpectedMarginCalls() {
        return expectedMarginCalls;
    }

    public void setExpectedMarginCalls(Set<MarginCall> expectedMarginCalls) {
        this.expectedMarginCalls = expectedMarginCalls;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getInterestPayment() {
        return interestPayment;
    }

    public void setInterestPayment(Double interestPayment) {
        this.interestPayment = interestPayment;
    }

    public Double getProductCashFlows() {
        return productCashFlows;
    }

    public void setProductCashFlows(Double productCashFlows) {
        this.productCashFlows = productCashFlows;
    }

    public Double getFeesCommissions() {
        return feesCommissions;
    }

    public void setFeesCommissions(Double feesCommissions) {
        this.feesCommissions = feesCommissions;
    }

    public Double getPendingCash() {
        return pendingCash;
    }

    public void setPendingCash(Double pendingCash) {
        this.pendingCash = pendingCash;
    }

    public Double getPAI() {
        return PAI;
    }

    public void setPAI(Double PAI) {
        this.PAI = PAI;
    }

    public Double getPendingNonCash() {
        return pendingNonCash;
    }

    public void setPendingNonCash(Double pendingNonCash) {
        this.pendingNonCash = pendingNonCash;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public Set<MarginCall> getReceviedMarginCalls() {
        return receviedMarginCalls;
    }

    public void setReceviedMarginCalls(Set<MarginCall> receviedMarginCalls) {
        this.receviedMarginCalls = receviedMarginCalls;
    }

    public String getMarginStatementId() {
        return marginStatementId;
    }

    public void setMarginStatementId(String marginStatementId) {
        this.marginStatementId = marginStatementId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getLegalEntityId() {
        return legalEntityId;
    }

    public void setLegalEntityId(String legalEntityId) {
        this.legalEntityId = legalEntityId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "MarginStatement{" +
                "totalAmount=" + totalAmount +
                ", legalEntityId='" + legalEntityId + '\'' +
                ", direction='" + direction + '\'' +
                ", date=" + date +
                ", interestPayment=" + interestPayment +
                ", productCashFlows=" + productCashFlows +
                ", feesCommissions=" + feesCommissions +
                ", pendingCash=" + pendingCash +
                ", PAI=" + PAI +
                ", pendingNonCash=" + pendingNonCash +
                ", marginStatementId='" + marginStatementId + '\'' +
                ", status='" + status + '\'' +
                ", agreement=" + agreement +
                ", receviedMarginCalls=" + receviedMarginCalls +
                ", expectedMarginCalls=" + expectedMarginCalls +
                '}';
    }
}
