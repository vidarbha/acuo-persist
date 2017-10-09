package com.acuo.persist.core;

import com.google.inject.Singleton;

import javax.inject.Inject;

@Singleton
public class ImportService {

    private final DataLoader loader;
    private final DataImporter importer;

    private String branch = null;

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
        deleteAll();
        importer.importFiles(branch, importer.filesToImport());
    }

    public void deleteAll() {
        loader.purgeDatabase();
    }
}
