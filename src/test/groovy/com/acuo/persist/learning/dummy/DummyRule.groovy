package com.acuo.persist.learning.dummy

import com.acuo.persist.entity.Entity
import groovy.transform.ToString
import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity

@NodeEntity
@ToString
class DummyRule implements Entity {

    @Id @GeneratedValue Long id

    Double haircut

}
