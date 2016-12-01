package com.acuo.persist.entity;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Account extends Entity{

    private String accountId;

    private String name;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
