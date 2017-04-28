package com.acuo.persist.core;

import org.neo4j.ogm.annotation.Transient;

@Transient
public interface DataImporter {

    String[] ALL_FILES = {
            "firms",
            "workingZones",
            "legalentities",
            "clearingHouses",
            "fcms",
            "tradingAccounts",
            "bilateralAgreements",
            "clearedAgreements",
            "ratingScores",
            "assetCategories",
            "custodianAccounts",
            "counterpartCustodianAccounts",
            "custodianAssets",
            "buildEligibility",
            "mstatements",
            "initmcexp",
            "initmc",
            "infopres",
            "settings",
            "assetTransfer",
            "currencies",
            "portfolios",
            "books"
    };

    void importFiles(final String branch, String... fileNames);

}