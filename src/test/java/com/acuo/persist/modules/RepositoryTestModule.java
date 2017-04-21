package com.acuo.persist.modules;

import com.acuo.persist.utils.ValuationHelper;
import com.google.inject.AbstractModule;

public class RepositoryTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ValuationHelper.class);
    }
}