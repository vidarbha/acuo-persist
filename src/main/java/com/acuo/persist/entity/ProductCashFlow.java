package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductCashFlow extends StatementItem<ProductCashFlow> {
    private Double totalCouponPayment;
    private Double upfrontFee;
    private Double premiumPayment;
    private Double cDSCreditEvent;
    private Double nDFCashSettlement;
}
