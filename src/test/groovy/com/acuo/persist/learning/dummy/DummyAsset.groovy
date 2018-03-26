package com.acuo.persist.learning.dummy

import com.acuo.persist.entity.Entity
import groovy.transform.ToString
import org.neo4j.ogm.annotation.*

@NodeEntity
@ToString
class DummyAsset implements Entity {

    @Id @GeneratedValue Long id

    @Property(name = "id")
    @Id
    String assetId

    @Relationship(type = "APPLIES_TO", direction = Relationship.INCOMING)
    Set<DummyRule> rules

    @Relationship(type = "HOLDS", direction = Relationship.INCOMING)
    DummyHolds holds

}
