package com.acuo.persist.modules;

import com.acuo.persist.consistency.BalanceChecker;
import com.acuo.persist.consistency.BalanceCheckerImpl;
import com.google.inject.AbstractModule;

public class ConsistencyModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BalanceChecker.class).to(BalanceCheckerImpl.class);
    }
}
