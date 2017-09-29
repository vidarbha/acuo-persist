package com.acuo.persist.modules;

import com.acuo.persist.consistency.CheckManager;
import com.acuo.persist.consistency.CheckManagerImpl;
import com.google.inject.AbstractModule;

public class ConsistencyModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CheckManager.class).to(CheckManagerImpl.class);
    }
}
