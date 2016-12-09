package com.acuo.persist.modules;

import com.acuo.persist.core.ImportService;
import com.acuo.persist.core.Neo4jImportService;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class ImportServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ImportService.class).to(Neo4jImportService.class).in(Singleton.class);
    }

}
