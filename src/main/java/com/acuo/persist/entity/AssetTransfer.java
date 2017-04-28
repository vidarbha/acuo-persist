package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDateTime;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"of"})
@ToString(exclude = {"of"})
public class AssetTransfer extends Entity<AssetTransfer> {

    @Property(name = "id")
    @Index(primary = true)
    private String assertTransferId;

    private Double quantities;
    private Double earmarkedQuuantities;
    private AssetTransferStatus status;
    private AssetTransferStatus subStatus;
    private String deliveryTime;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime pledgeTime;

    private Double transferValue;

    @Relationship(type = "OF", direction = Relationship.OUTGOING)
    private Asset of;

    @Relationship(type = "FROM", direction = Relationship.OUTGOING)
    private CustodianAccount from;

    @Relationship(type = "TO", direction = Relationship.OUTGOING)
    private CustodianAccount to;

    @Relationship(type = "GENERATED_BY", direction = Relationship.INCOMING)
    private MarginCall generatedBy;

}
