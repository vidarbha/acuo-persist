package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateConverter;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;

@RelationshipEntity(type="VALUE")
@Getter
@Setter
public class ValueRelation extends Entity<ValueRelation>{

    @StartNode
    private Valuation valuation;

    @EndNode
    private Value value;

    @Convert(LocalDateConverter.class)
    private LocalDate dateTime;

    @Override
    public String toString() {
        return "ValueRelation{" +
                "dateTime=" + dateTime +
                '}';
    }
}
