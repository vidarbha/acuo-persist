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
    private String assetId;
    private Double totalQuantities;
    private AssetTransferStatus status;
    private AssetTransferStatus subStatus;
    private Double earmarkedQuuantities;

    @Relationship(type = "SENDS", direction = Relationship.INCOMING)
    private CustodianAccount clientCustodian;

    @Relationship(type = "TO", direction = Relationship.OUTGOING)
    private CustodianAccount counterpartCustodian;

    @Relationship(type = "TO_FULFILL", direction = Relationship.OUTGOING)
    private MarginCall marginCall;

}
