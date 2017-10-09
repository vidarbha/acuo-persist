package com.acuo.persist.modules;

import com.acuo.persist.core.DataImporter;
import com.acuo.persist.core.Neo4jDataImporter;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

class DataImporterModule extends AbstractModule {

    @Override
    protected void configure() {

        install(new DataLoaderModule());

        bind(DataImporter.class).to(Neo4jDataImporter.class).in(Singleton.class);
    }
}
