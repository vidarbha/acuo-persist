package com.acuo.persist.entity;

import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.enums.AssetTransferStatus;
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
    private Double unitValue;
    private Double totalHaircut;

    private Double assetFxRate;
    private Double callFxRate;

    @Relationship(type = "OF")
    private Asset of;

    @Relationship(type = "FROM")
    private CustodianAccount from;

    @Relationship(type = "TO")
    private CustodianAccount to;

    @Relationship(type = "GENERATED_BY")
    private MarginCall generatedBy;

    public static Types.BalanceStatus status(AssetTransferStatus status) {
        switch (status) {
            case Departed:
                return Types.BalanceStatus.Pending;
            case InFlight:
                return Types.BalanceStatus.Pending;
            case Delayed:
                return Types.BalanceStatus.Pending;
            case Cancelled:
                return Types.BalanceStatus.Settled;
            case Deployed:
                return Types.BalanceStatus.Settled;
            case Available:
                return Types.BalanceStatus.Settled;
            case Arriving:
                return Types.BalanceStatus.Pending;
        }
        return null;
    }

}
