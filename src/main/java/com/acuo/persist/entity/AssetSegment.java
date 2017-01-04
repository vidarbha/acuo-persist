package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class AssetSegment extends Entity<AssetSegment>{

    private String assetId;
    private Double quantities;
    @Property(name = "id")
    private String assetSegmentId;
    private String subStatus;
    private String status;

}
