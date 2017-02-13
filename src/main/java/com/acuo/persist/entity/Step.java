package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Step extends Entity<Step> {

    private CallStatus status;

    @Relationship(type = "NEXT")
    private Next next;
}
