package com.acuo.persist.modules;

import com.acuo.persist.services.IRSService;
import com.acuo.persist.services.IRSServiceImpl;
import com.acuo.persist.services.TradeService;
import com.acuo.persist.services.TradeServiceImpl;
import com.google.inject.AbstractModule;

public class RepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IRSService.class).to(IRSServiceImpl.class);
        bind(TradeService.class).to(TradeServiceImpl.class);
    }

}