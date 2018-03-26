package com.acuo.persist.learning.dummy

import com.acuo.common.model.margin.Types
import com.acuo.persist.entity.Entity
import groovy.transform.ToString
import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Relationship

@NodeEntity
@ToString
class DummyAssetPledge implements Entity {

    @Id @GeneratedValue Long id

    Types.MarginType marginType

    @Relationship(type = "OF")
    DummyAsset asset

}
