package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;

@RelationshipEntity(type="VALUE")
@Data
@EqualsAndHashCode(callSuper = false)
public class ValueRelation<V extends Value> extends Entity<ValueRelation<V>>{

    @StartNode
    private Valuation<V> valuation;

    @EndNode
    private V value;

    @Convert(LocalDateConverter.class)
    private LocalDate dateTime;
}
