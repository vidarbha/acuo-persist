package com.acuo.persist.modules;

import com.acuo.persist.entity.FRA;
import com.acuo.persist.entity.IRS;
import com.acuo.persist.entity.Trade;
import com.acuo.persist.services.AgreementService;
import com.acuo.persist.services.AgreementServiceImpl;
import com.acuo.persist.services.AssetService;
import com.acuo.persist.services.AssetServiceImpl;
import com.acuo.persist.services.AssetTransferService;
import com.acuo.persist.services.AssetTransferServiceImpl;
import com.acuo.persist.services.AssetValuationService;
import com.acuo.persist.services.AssetValuationServiceImpl;
import com.acuo.persist.services.ClientService;
import com.acuo.persist.services.ClientServiceImpl;
import com.acuo.persist.services.CounterpartService;
import com.acuo.persist.services.CounterpartServiceImpl;
import com.acuo.persist.services.CurrencyService;
import com.acuo.persist.services.CurrencyServiceImpl;
import com.acuo.persist.services.CustodianAccountService;
import com.acuo.persist.services.CustodianAccountServiceImpl;
import com.acuo.persist.services.DisputeService;
import com.acuo.persist.services.DisputeServiceImpl;
import com.acuo.persist.services.FXRateService;
import com.acuo.persist.services.FXRateServiceImpl;
import com.acuo.persist.services.FXValueService;
import com.acuo.persist.services.FXValueServiceImpl;
import com.acuo.persist.services.LegalEntityService;
import com.acuo.persist.services.LegalEntityServiceImpl;
import com.acuo.persist.services.MarginCallService;
import com.acuo.persist.services.MarginCallServiceImpl;
import com.acuo.persist.services.MarginStatementService;
import com.acuo.persist.services.MarginStatementServiceImpl;
import com.acuo.persist.services.NextService;
import com.acuo.persist.services.NextServiceImpl;
import com.acuo.persist.services.PortfolioService;
import com.acuo.persist.services.PortfolioServiceImpl;
import com.acuo.persist.services.StatementItemService;
import com.acuo.persist.services.StatementItemServiceImpl;
import com.acuo.persist.services.StepService;
import com.acuo.persist.services.StepServiceImpl;
import com.acuo.persist.services.TradeService;
import com.acuo.persist.services.TradeServiceImpl;
import com.acuo.persist.services.TradingAccountService;
import com.acuo.persist.services.TradingAccountServiceImpl;
import com.acuo.persist.services.ValuationService;
import com.acuo.persist.services.ValuationServiceImpl;
import com.acuo.persist.services.ValueService;
import com.acuo.persist.services.ValueServiceImpl;
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
        bind(CurrencyService.class).to(CurrencyServiceImpl.class);
        bind(StatementItemService.class).to(StatementItemServiceImpl.class);
        bind(DisputeService.class).to(DisputeServiceImpl.class);
        bind(AssetValuationService.class).to(AssetValuationServiceImpl.class);
        bind(StepService.class).to(StepServiceImpl.class);
        bind(NextService.class).to(NextServiceImpl.class);
        bind(FXRateService.class).to(FXRateServiceImpl.class);
        bind(FXValueService.class).to(FXValueServiceImpl.class);
    }

}