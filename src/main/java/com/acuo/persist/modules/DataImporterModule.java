package com.acuo.persist.modules;

import com.acuo.persist.core.DataImporter;
import com.acuo.persist.core.Neo4jDataImporter;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class DataImporterModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DataImporter.class).to(Neo4jDataImporter.class).in(Singleton.class);
    }

}
