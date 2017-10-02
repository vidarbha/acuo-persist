package com.acuo.persist.modules;

import com.acuo.persist.validations.CheckManager;
import com.acuo.persist.validations.CheckManagerImpl;
import com.acuo.persist.validations.Checker;
import com.google.inject.AbstractModule;

public class ConsistencyModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Checker.class);
        bind(CheckManager.class).to(CheckManagerImpl.class);
    }
}
