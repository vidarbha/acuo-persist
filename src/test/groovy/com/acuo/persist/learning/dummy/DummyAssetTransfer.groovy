package com.acuo.persist.learning.dummy

import com.acuo.persist.entity.Entity
import groovy.transform.ToString
import org.neo4j.ogm.annotation.*

@NodeEntity
@ToString
class DummyAssetTransfer implements Entity {

    @Id @GeneratedValue Long id

    @Property(name = "id")
    @Id
    String assertTransferId

    @Relationship(type = "OF")
    DummyAsset of

    @Relationship(type = "FROM")
    DummyCustodianAccount from

    @Relationship(type = "GENERATED_BY")
    DummyMarginCall generatedBy
}
