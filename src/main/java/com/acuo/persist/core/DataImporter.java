package com.acuo.persist.core;

import org.neo4j.ogm.annotation.Transient;

@Transient
public interface DataImporter {

    void createConstraints(String branch);

    void importFiles(final String branch, String client, String... fileNames);

    String[] filesToImport();

}