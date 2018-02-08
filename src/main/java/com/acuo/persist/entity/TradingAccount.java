package com.acuo.persist.entity;

import com.acuo.persist.entity.trades.Trade;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;
import java.util.TreeSet;

@NodeEntity
@Data
@EqualsAndHashCode(exclude = {"id"})
public class TradingAccount implements Entity<TradingAccount> {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name="id")
    private String accountId;

    private String name;

    private String shortName;

    @Relationship(type = "POSITIONS_ON")
    private Set<Trade> trades = new TreeSet<>();

    @Relationship(type = "ACCESSES")
    private Set<CustodianAccount> custodianAccounts;

    public void remove(Trade trade) {
        trades.remove(trade);
    }

    public void add(Trade trade) {
        trades.add(trade);
    }

}
