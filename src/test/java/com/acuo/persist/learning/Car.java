package com.acuo.persist.learning;

import com.acuo.persist.entity.Entity;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
class Car extends Entity {
    Car() {
    }

    @Property
    String type;
    @Relationship(type = "HAS_CAR", direction = "INCOMING")
    Person owner;
}
