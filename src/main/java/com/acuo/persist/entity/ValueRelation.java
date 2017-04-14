package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;

@RelationshipEntity(type="VALUE")
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class ValueRelation extends Entity<ValueRelation>{

    @StartNode
    private Valuation valuation;

    @Convert(LocalDateConverter.class)
    private LocalDate dateTime;
}
