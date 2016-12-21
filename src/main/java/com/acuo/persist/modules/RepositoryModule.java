package com.acuo.persist.modules;

import com.acuo.persist.entity.FRA;
import com.acuo.persist.entity.IRS;
import com.acuo.persist.entity.Trade;
import com.acuo.persist.services.*;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class RepositoryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountService.class).to(AccountServiceImpl.class);
        bind(new TypeLiteral<TradeService<Trade>>(){}).to(new TypeLiteral<TradeServiceImpl<Trade>>(){});
        bind(new TypeLiteral<TradeService<IRS>>(){}).to(new TypeLiteral<TradeServiceImpl<IRS>>(){});
        bind(new TypeLiteral<TradeService<FRA>>(){}).to(new TypeLiteral<TradeServiceImpl<FRA>>(){});
        bind(PortfolioService.class).to(PortfolioServiceImpl.class);
        bind(ValuationService.class).to(ValuationServiceImpl.class);
        bind(ValueService.class).to(ValueServiceImpl.class);
    }

}