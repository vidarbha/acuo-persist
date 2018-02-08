package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity
@Data
@EqualsAndHashCode(exclude = {"id"})
public class AssetSegment implements Entity<AssetSegment> {

    @Id
    @GeneratedValue
    private Long id;
    @Property(name = "id")
    @Id
    private String assetSegmentId;
    private String assetId;
    private Double quantities;
    private String subStatus;
    private String status;

}
