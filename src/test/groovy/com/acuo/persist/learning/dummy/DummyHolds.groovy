package com.acuo.persist.learning.dummy

import com.acuo.persist.entity.Entity
import groovy.transform.ToString
import org.neo4j.ogm.annotation.*

@RelationshipEntity(type = "HOLDS")
@ToString(excludes = ["custodianAccount","asset"])
class DummyHolds implements Entity<DummyHolds> {

    @Id
    @GeneratedValue
    Long id

    @StartNode
    DummyCustodianAccount custodianAccount

    @EndNode
    DummyAsset asset

    Double availableQuantity
}

