package com.acuo.persist.core;

import org.neo4j.ogm.annotation.Transient;

@Transient
public interface DataImporter {

    static final String[] ALL_FILES = {
            "clients",
            "legalentities",
            "tradingAccounts",
            "clearingHouses",
            "fcms",
            "clearedAgreements",
            "bilateralAgreements",
            "mstatements",
            "initmcexp",
            "initmc",
            "settings"
    };

    void importFiles(String... fileNames);

}