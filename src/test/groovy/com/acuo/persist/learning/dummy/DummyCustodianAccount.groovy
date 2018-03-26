package com.acuo.persist.learning.dummy

import com.acuo.persist.entity.Entity
import groovy.transform.ToString
import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property

@NodeEntity
@ToString
class DummyCustodianAccount implements Entity<DummyCustodianAccount> {

    @Id
    @GeneratedValue
    Long id

    @Property(name = "id")
    @Id
    String accountId

}