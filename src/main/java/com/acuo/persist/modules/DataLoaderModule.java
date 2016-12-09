package com.acuo.persist.modules;

import com.acuo.persist.core.DataLoader;
import com.acuo.persist.core.SessionDataLoader;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class DataLoaderModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DataLoader.class).to(SessionDataLoader.class).in(Singleton.class);
    }

}