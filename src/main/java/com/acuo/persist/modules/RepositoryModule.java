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
        bind(TradingAccountService.class).to(TradingAccountServiceImpl.class);
        bind(new TypeLiteral<TradeService<Trade>>(){}).to(new TypeLiteral<TradeServiceImpl<Trade>>(){});
        bind(new TypeLiteral<TradeService<IRS>>(){}).to(new TypeLiteral<TradeServiceImpl<IRS>>(){});
        bind(new TypeLiteral<TradeService<FRA>>(){}).to(new TypeLiteral<TradeServiceImpl<FRA>>(){});
        bind(PortfolioService.class).to(PortfolioServiceImpl.class);
        bind(ValuationService.class).to(ValuationServiceImpl.class);
        bind(ValueService.class).to(ValueServiceImpl.class);
        bind(ClientService.class).to(ClientServiceImpl.class);
        bind(LegalEntityService.class).to(LegalEntityServiceImpl.class);
        bind(AgreementService.class).to(AgreementServiceImpl.class);
        bind(MarginStatementService.class).to(MarginStatementServiceImpl.class);
        bind(CounterpartService.class).to(CounterpartServiceImpl.class);
        bind(AssetService.class).to(AssetServiceImpl.class);
        bind(MarginCallService.class).to(MarginCallServiceImpl.class);
        bind(AssetTransferService.class).to(AssetTransferServiceImpl.class);
        bind(CustodianAccountService.class).to(CustodianAccountServiceImpl.class);
    }

}