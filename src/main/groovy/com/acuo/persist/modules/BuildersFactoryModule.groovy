package com.acuo.persist.modules

import com.acuo.persist.builders.*
import com.acuo.persist.factories.AgreementFactory
import com.acuo.persist.factories.BuilderFactory
import com.acuo.persist.factories.ClientFactory
import com.acuo.persist.factories.ClientSignsFactory
import com.acuo.persist.factories.CustodianAccountFactory
import com.acuo.persist.factories.LegalEntityFactory
import com.acuo.persist.factories.StatementFactory
import com.acuo.persist.factories.ListFactory
import com.acuo.persist.factories.StepFactory
import com.acuo.persist.factories.VariationMarginFactory
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
        binder.addBinding().to(StepFactory)
        binder.addBinding().to(CustodianAccountFactory)
    }
}
