package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductCashFlow extends StatementItem<ProductCashFlow> {
    @Property(name="id")
    private String productCashFlowId;
    private Double totalCouponPayment;
    private Double upfrontFee;
    private Double premiumPayment;
    private Double cDSCreditEvent;
    private Double nDFCashSettlement;

}
