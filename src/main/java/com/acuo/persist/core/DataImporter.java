package com.acuo.persist.core;

import org.neo4j.ogm.annotation.Transient;

@Transient
public interface DataImporter {

    static final String[] ALL_FILES = {"clients", "ccps", "counterparts", "funds", "portfolios", "exposures"};

    void importFiles(String... fileNames);

}