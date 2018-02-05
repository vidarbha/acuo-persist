package com.acuo.persist.modules;

import com.acuo.persist.core.DataImporter;
import com.acuo.persist.core.DataLoader;
import com.acuo.persist.core.ImportService;
import com.acuo.persist.core.Neo4jDataImporter;
import com.acuo.persist.utils.DirectDataLoader;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

public class ImportTestServiceModule extends AbstractModule {

    @Override
    protected void configure() {

        install(new RepositoryModule());

        bind(DataImporter.class).to(Neo4jDataImporter.class).in(Singleton.class);
        bind(DataLoader.class).to(DirectDataLoader.class).in(Singleton.class);

        bind(ImportService.class).in(Singleton.class);
    }
}