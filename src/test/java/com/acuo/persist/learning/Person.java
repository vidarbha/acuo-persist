package com.acuo.persist.learning;

import com.acuo.persist.entity.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(exclude = "id")
class Person implements Entity<Person> {
    Person() {
    }

    @Id
    @GeneratedValue
    Long id;

    @Relationship(type = "HAS_CAR")
    Set<Car> cars = new HashSet<>();
}
