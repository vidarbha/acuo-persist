package com.acuo.persist.modules;

import com.google.inject.AbstractModule;

public class ImportServiceModule extends AbstractModule {

    @Override
    protected void configure() {

        install(new RepositoryModule());
        install(new DataImporterModule());
    }
}
