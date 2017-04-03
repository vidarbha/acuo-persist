package com.acuo.persist.learning;

import com.acuo.persist.entity.Entity;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
class Person extends Entity<Person> {
    Person() {
    }

    @Relationship(type = "HAS_CAR")
    Set<Car> cars = new HashSet<>();
}
