package com.acuo.persist.learning;

import com.acuo.persist.entity.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@Data
@EqualsAndHashCode(exclude = {"owner"})
@ToString(exclude = "owner")
class Car implements Entity {
    Car() {
    }

    @Id
    @GeneratedValue
    Long id;

    @Property
    String type;

    @Relationship(type = "HAS_CAR", direction = "INCOMING")
    Person owner;
}
