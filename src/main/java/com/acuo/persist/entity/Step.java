package com.acuo.persist.entity;

import com.acuo.persist.entity.enums.StatementStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
@EqualsAndHashCode(exclude = "id")
public class Step implements Entity<Step> {

    @Id
    @GeneratedValue
    private Long id;

    private StatementStatus status;

    @Relationship(type = "NEXT")
    private Next next;
}
