package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDateTime;

@RelationshipEntity(type = "NEXT")
@Getter
@Setter
public class Next implements Entity<Next> {

    @Id
    @GeneratedValue
    private Long id;

    @Convert(LocalDateTimeConverter.class)
    private LocalDateTime time;

    @StartNode
    private Step start;

    @EndNode
    private Step end;
}
