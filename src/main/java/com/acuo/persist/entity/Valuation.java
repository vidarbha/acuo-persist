package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Valuation extends Entity<Valuation> {

    @Convert(LocalDateConverter.class)
    private LocalDate date;

    @Relationship(type = "VALUE")
    private Set<Value> values;

    @Property(name = "id")
    private String valuationId;
}
