package com.acuo.persist.modules

import com.acuo.persist.builders.FactoryBuilder
import com.acuo.persist.factories.*
import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder

class BuildersFactoryModule extends AbstractModule {

    @Override
    void configure() {

        bind(FactoryBuilder)

        Multibinder binder = Multibinder.newSetBinder(binder(), BuilderFactory)
        binder.addBinding().to(ListFactory)
        binder.addBinding().to(StatementFactory)
        binder.addBinding().to(AgreementFactory)
        binder.addBinding().to(VariationMarginFactory)
        binder.addBinding().to(ClientFactory)
        binder.addBinding().to(LegalEntityFactory)
        binder.addBinding().to(ClientSignsFactory)
        binder.addBinding().to(CounterpartySignsFactory)
        binder.addBinding().to(StepFactory)
        binder.addBinding().to(CustodianAccountFactory)
        binder.addBinding().to(CollateralFactory)
        binder.addBinding().to(CollateralValueFactory)
    }
}
