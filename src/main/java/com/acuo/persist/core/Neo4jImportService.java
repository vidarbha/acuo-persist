package com.acuo.persist.core;

import com.google.inject.Singleton;

import javax.inject.Inject;

@Singleton
public class Neo4jImportService implements ImportService {

    private final DataLoader loader;
    private final DataImporter importer;

    @Inject
    public Neo4jImportService(DataLoader loader, DataImporter importer) {
        this.loader = loader;
        this.importer = importer;
    }

    @Override
    public void reload() {
        loader.purgeDatabase();
        importer.importFiles(DataImporter.ALL_FILES);
    }

    @Override
    public void reload(String... fileNames) {
        loader.purgeDatabase();
        loader.createConstraints();
        importer.importFiles(fileNames);
    }
}
