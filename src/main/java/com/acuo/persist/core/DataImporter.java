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
            "custodians",
            "custodianAccounts",
            "counterpartCustodianAccounts",
            "custodianAssets",
            "mstatements",
            "initmcexp",
            "initmc",
            "settings",
            "assetTransfer",
            "portfolios"
    };

    void importFiles(String... fileNames);

}