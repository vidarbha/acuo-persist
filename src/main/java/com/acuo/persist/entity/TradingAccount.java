package com.acuo.persist.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false)
public class TradingAccount extends Entity<TradingAccount> {

    @Index(unique = true)
    @Property(name="id")
    private String accountId;

    private String name;

    private String shortName;

    @Relationship(type = "POSITIONS_ON")
    private Set<Trade> trades = new TreeSet<>();

    @Relationship(type = "ACCESSES")
    private Set<CustodianAccount> custodianAccounts;

    public boolean add(Trade trade) {
        return trades.add(trade);
    }

}
