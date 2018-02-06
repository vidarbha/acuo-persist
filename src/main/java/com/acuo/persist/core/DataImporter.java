package com.acuo.persist.core;

import lombok.Builder;
import org.neo4j.ogm.annotation.Transient;

@Transient
public interface DataImporter {

    void reload();

    void reload(String... clients);

    void load(String fileName);

    void load(String client, String... fileNames);

    void createConstraints();

    void deleteAll();

    DataImporter withBranch(String branch);

    @Builder
    class DataItem {
        String branch;
        String[] clients;
        String[] fileNames;
    }

}