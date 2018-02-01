package com.acuo.persist.core;

import com.google.inject.Singleton;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
@Singleton
public class ImportService {

    private final DataLoader loader;
    private final DataImporter importer;

    @Inject
    public ImportService(DataLoader loader, DataImporter importer) {
        this.loader = loader;
        this.importer = importer;
    }

    public void reload() {
        deleteAll();
        DataFiles dataFiles = DataFiles.builder().client("ACUO").build();
        reload(dataFiles);
    }

    public void load(String fileName) {
        DataFiles dataFiles = DataFiles.builder().client("ACUO").fileName(fileName).build();
        load(dataFiles);
    }

    public void load(DataFiles dataFiles) {
        importer.importFiles(dataFiles.branch, dataFiles.client, dataFiles.fileName);
    }

    public void reload(DataFiles dataFiles) {
        importer.importFiles(dataFiles.branch, dataFiles.client, importer.filesToImport());
    }

    public void deleteAll() {
        log.info("purging the database");
        loader.purgeDatabase();
    }

    @Builder
    public static class DataFiles {
        private String branch;
        @Builder.Default
        private String client = "ACUO";
        private String fileName;
    }
}
