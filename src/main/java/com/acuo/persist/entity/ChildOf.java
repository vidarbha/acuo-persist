package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDateTime;

@RelationshipEntity(type="CHILD_OF")
@Getter
@Setter
public class ChildOf extends Entity<ChildOf> {

    @StartNode
    private MarginCall child;

    @EndNode
    private MarginCall parent;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime time;
}
