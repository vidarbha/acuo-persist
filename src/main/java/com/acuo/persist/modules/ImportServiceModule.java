package com.acuo.persist.modules;

import com.acuo.persist.core.ImportService;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class ImportServiceModule extends AbstractModule {

    @Override
    protected void configure() {

        install(new RepositoryModule());
        install(new DataImporterModule());

        bind(ImportService.class).in(Singleton.class);
    }
}
