package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@NodeEntity
public class MarginStatement extends Entity{




//    @DateString(value = "dd/MM/yy")
//    private Date date;

//    private Double interestPayment;
//
//    private BigDecimal productCashFlows;
//
//    private Double feesCommissions;
//
//    private Double pendingCash;
//
//    private Double PAI;
//
//    private Double pendingNonCash;

//    @Property(name="id")
//    private String marginStatementId;

//    private String status;

    //@Relationship(type = "STEMS_FROM")
    //private Agreement agreement;

    //@Relationship(type = "PART_OF")
    //private Set<MarginCall> marginCalls;

//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }

//    public Double getInterestPayment() {
//        return interestPayment;
//    }
//
//    public void setInterestPayment(Double interestPayment) {
//        this.interestPayment = interestPayment;
//    }
//
//    public BigDecimal getProductCashFlows() {
//        return productCashFlows;
//    }
//
//    public void setProductCashFlows(BigDecimal productCashFlows) {
//        this.productCashFlows = productCashFlows;
//    }
//
//    public Double getFeesCommissions() {
//        return feesCommissions;
//    }
//
//    public void setFeesCommissions(Double feesCommissions) {
//        this.feesCommissions = feesCommissions;
//    }
//
//    public Double getPendingCash() {
//        return pendingCash;
//    }
//
//    public void setPendingCash(Double pendingCash) {
//        this.pendingCash = pendingCash;
//    }
//
//    public Double getPAI() {
//        return PAI;
//    }
//
//    public void setPAI(Double PAI) {
//        this.PAI = PAI;
//    }
//
//    public Double getPendingNonCash() {
//        return pendingNonCash;
//    }
//
//    public void setPendingNonCash(Double pendingNonCash) {
//        this.pendingNonCash = pendingNonCash;
//    }


//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }

//    public Agreement getAgreement() {
//        return agreement;
//    }
//
//    public void setAgreement(Agreement agreement) {
//        this.agreement = agreement;
//    }
//
//    public Set<MarginCall> getMarginCalls() {
//        return marginCalls;
//    }
//
//    public void setMarginCalls(Set<MarginCall> marginCalls) {
//        this.marginCalls = marginCalls;
//    }

//    public String getMarginStatementId() {
//        return marginStatementId;
//    }
//
//    public void setMarginStatementId(String marginStatementId) {
//        this.marginStatementId = marginStatementId;
//    }
//
//    @Override
//    public String toString() {
//        return "MarginStatement{" +
////                "date=" + date +
////                ", interestPayment=" + interestPayment +
////                ", productCashFlows=" + productCashFlows +
////                ", feesCommissions=" + feesCommissions +
////                ", pendingCash=" + pendingCash +
////                ", PAI=" + PAI +
////                ", pendingNonCash=" + pendingNonCash +
////                ", marginStatementId='" + marginStatementId + '\'' +
//  //              ", status='" + status + '\'' +
//   //             ", agreement=" + agreement +
//  //              ", marginCalls=" + marginCalls +
//                '}';
//    }
}
