package com.acuo.persist.entity;

import com.acuo.persist.entity.enums.StatementStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class Step extends Entity<Step> {

    private StatementStatus status;

    @Relationship(type = "NEXT")
    private Next next;
}
