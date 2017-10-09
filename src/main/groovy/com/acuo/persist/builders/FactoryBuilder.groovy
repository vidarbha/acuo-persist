package com.acuo.persist.builders

import com.acuo.persist.factories.BuilderFactory

import javax.inject.Inject


class FactoryBuilder extends FactoryBuilderSupport {

    @Inject
    FactoryBuilder(Set<BuilderFactory> factories) {
        factories.each { f -> registerFactory(f.name(), f)}
    }
}