package com.acuo.persist.builders

import javax.inject.Inject


class StatementsFactoryBuilder extends FactoryBuilderSupport {

    @Inject
    StatementsFactoryBuilder(Set<BuilderFactory> factories) {
        factories.each { f -> registerFactory(f.name(), f)}
    }
}