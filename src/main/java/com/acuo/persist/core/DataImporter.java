package com.acuo.persist.core;

import org.neo4j.ogm.annotation.Transient;

@Transient
public interface DataImporter {

    String[] ALL_FILES = {
            "firms",
            "legalentities",
            "tradingAccounts",
            "clearingHouses",
            "fcms",
            "clearedAgreements",
            "bilateralAgreements",
            "assetCategories",
            "assetInventory",
            "custodians",
            "custodianAccounts",
            "counterpartCustodians",
            "counterpartCustodianAccounts",
            "custodianAssets",
            "mstatements",
            "initmcexp",
            "initmc",
            "settings"
    };

    void importFiles(String... fileNames);

}