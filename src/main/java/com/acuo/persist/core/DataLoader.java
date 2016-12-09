package com.acuo.persist.core;

import org.neo4j.ogm.annotation.Transient;

@Transient
public interface DataLoader {

    void purgeDatabase();

    void createConstraints();

    void loadData(String query);

    void loadDataFile(String fileName);

    void loadAll();
}