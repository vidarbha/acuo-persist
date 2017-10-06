package com.acuo.persist.modules

import com.acuo.persist.builders.*
import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder

class BuildersFactoryModule extends AbstractModule {

    @Override
    void configure() {

        bind(StatementsFactoryBuilder)

        Multibinder binder = Multibinder.newSetBinder(binder(), BuilderFactory)
        binder.addBinding().to(StatementsFactory)
        binder.addBinding().to(StatementFactory)
        binder.addBinding().to(AgreementFactory)
        binder.addBinding().to(VariationMarginFactory)
    }
}
