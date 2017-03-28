package com.acuo.persist.entity;

import com.acuo.persist.neo4j.converters.LocalDateConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.annotation.typeconversion.DateString;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.neo4j.ogm.annotation.Relationship.INCOMING;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"values"})
@ToString(exclude = {"values"})
public class Valuation<T extends Valuation> extends Entity<T> {



    @Property(name = "id")
    private String valuationId;

    @Relationship(type = "VALUE")
    private Set<ValueRelation> values;


}
