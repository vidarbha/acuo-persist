package com.acuo.persist.core;

import org.neo4j.ogm.annotation.Transient;

@Transient
public interface DataImporter {

    String[] ALL_FILES = {
            "firms",
            "legalentities",
            "currencies",
            "tradingAccounts",
            "clearingHouses",
            "fcms",
            "clearedAgreements",
            "bilateralAgreements",
            "assetCategories",
            "custodianAccounts",
            "counterpartCustodianAccounts",
            "custodianAssets",
            "buildEligibility",
            "mstatements",
            "initmcexp",
            "initmc",
            "settings",
            "assetTransfer",
            "portfolios",
            "infopres"
    };

    void importFiles(final String branch, String... fileNames);

}