package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class Account extends Entity{

    @Property(name="id")
    private String accountId;

    private String name;

    @Relationship(type = "POSITIONS_ON", direction = Relationship.OUTGOING)
    private Set<Trade> trades = new HashSet<>();

    public boolean add(Trade trade) {
        return trades.add(trade);
    }
}
