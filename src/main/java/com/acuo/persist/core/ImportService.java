package com.acuo.persist.core;

import com.google.inject.Singleton;

@Singleton
@Deprecated
/**
 * @deprecated use {@link DataImporter}
 */
public interface ImportService {

    void reload();

    void reload(String... clients);

    void load(String fileName);

    void load(String client, String... fileNames);

    void createConstraints();

    void deleteAll();
}