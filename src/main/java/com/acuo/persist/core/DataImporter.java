package com.acuo.persist.core;

import org.neo4j.ogm.annotation.Transient;

@Transient
public interface DataImporter {

    void importFiles(final String branch, String... fileNames);

    String[] filesToImport();

}