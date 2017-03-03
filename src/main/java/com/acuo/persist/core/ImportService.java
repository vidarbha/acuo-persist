package com.acuo.persist.core;

import com.google.inject.Singleton;

import javax.inject.Inject;

@Singleton
public class ImportService {

    private final DataLoader loader;
    private final DataImporter importer;

    public static final String DEFAULT_BRANCH = "master";
    private String branch = DEFAULT_BRANCH;

    @Inject
    public ImportService(DataLoader loader, DataImporter importer) {
        this.loader = loader;
        this.importer = importer;
    }

    public ImportService branch(String value) {
        this.branch = value;
        return this;
    }

    public void load(String fileName) {
        importer.importFiles(branch, fileName);
    }

    public void reload() {
        loader.purgeDatabase();
        importer.importFiles(branch, DataImporter.ALL_FILES);
    }
}
