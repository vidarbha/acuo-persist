package com.acuo.persist.core;

import com.google.inject.Singleton;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.Arrays;

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

    public void createConstraints() {
        log.info("create constraints");
        importer.createConstraints("develop");
    }

    public void reload() {
        deleteAll();
        createConstraints();
        reload("ACUO", "Reuters", "Palo");
    }

    public void load(String fileName) {
        load("ACUO", fileName);
    }

    public void reload(String... clients) {
        Arrays.stream(clients).forEach(client -> {
                DataFiles dataFiles = DataFiles.builder().client(client).build();
                reload(dataFiles);
            }
        );
    }

    public void load(String client, String... fileNames) {
        final DataFiles.DataFilesBuilder builder = DataFiles.builder().client(client);
        Arrays.stream(fileNames).forEach(file -> {
                DataFiles dataFiles = builder.fileName(file).build();
                load(dataFiles);
            }
        );
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