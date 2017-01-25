package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class AssetTransfer extends Entity<AssetTransfer>{

    @Property(name = "id")
    private String assertTransferId;

    private Double quantities;
    private Double earmarkedQuuantities;
    private AssetTransferStatus status;
    private AssetTransferStatus subStatus;
    private String deliveryTime;

    @Relationship(type = "FROM", direction = Relationship.OUTGOING)
    private CustodianAccount from;

    @Relationship(type = "TO", direction = Relationship.OUTGOING)
    private CustodianAccount to;

    @Relationship(type = "GENERATED_BY", direction = Relationship.INCOMING)
    private MarginCall generatedBy;

}
