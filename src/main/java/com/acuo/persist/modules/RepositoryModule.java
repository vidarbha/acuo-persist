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

        install(new Neo4jPersistModule());

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
        bind(SettlementService.class).to(SettlementServiceImpl.class);
        bind(SettlementDateService.class).to(SettlementDateServiceImpl.class);
        bind(CollateralService.class).to(CollateralServiceImpl.class);
        bind(CollateralValueService.class).to(CollateralValueServiceImpl.class);
        bind(ErrorService.class).to(ErrorServiceImpl.class);
        bind(AssetPledgeService.class).to(AssetPledgeServiceImpl.class);
        bind(AssetPledgeValueService.class).to(AssetPledgeValueServiceImpl.class);
        bind(ClientSignsRelationService.class).to(ClientSignsRelationServiceImpl.class);
        bind(CounterpartSignsRelationService.class).to(CounterpartSignsRelationServiceImpl.class);
        bind(RuleService.class).to(RuleServiceImpl.class);
        bind(CustodianService.class).to(CustodianServiceImpl.class);
        bind(HoldsService.class).to(HoldsServiceImpl.class);
    }

}