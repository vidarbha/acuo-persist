package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateString;
import org.neo4j.ogm.annotation.typeconversion.EnumString;

import java.util.Date;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Exposure extends Entity<Exposure> {

    private String positionId;

    private String note;

    @EnumString(value = ProductType.class)
    private ProductType productType;

    @DateString(value = "yyyy-MM-dd")
    private Date tradeDate;

    @DateString(value = "yyyy-MM-dd")
    private Date effectiveDate;

    @DateString(value = "yyyy-MM-dd")
    private Date maturityDate;

    @DateString(value = "yyyy-MM-dd")
    private Date clearingDate;

    @EnumString(value = Direction.class)
    private Direction direction;

    @EnumString(value = Status.class)
    private Status status;

    private String source;

    @Relationship(type = "IS_A")
    private Product product;

    @Relationship(type = "IS_DEALT_WITH")
    private Counterpart counterpart;

    @Relationship(type = "IS_CLEARED_WITH")
    private ClearingHouse clearingHouse;

}
