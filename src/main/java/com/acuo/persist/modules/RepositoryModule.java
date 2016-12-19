package com.acuo.persist.modules;

import com.acuo.persist.services.*;
import com.google.inject.AbstractModule;

public class RepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FRAService.class).to(FRAServiceImpl.class);
        bind(IRSService.class).to(IRSServiceImpl.class);
        bind(AccountService.class).to(AccountServiceImpl.class);
        bind(TradeService.class).to(TradeServiceImpl.class);
        bind(PortfolioService.class).to(PortfolioServiceImpl.class);
        bind(ValuationService.class).to(ValuationServiceImpl.class);
        bind(ValueService.class).to(ValueServiceImpl.class);
    }

}