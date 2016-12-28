package com.acuo.persist.spring;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;

import javax.inject.Inject;

public class SpringGuiceModule extends AbstractModule {

    @Inject
    Injector injector;

    @Override
    protected void configure() {
        /*AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfiguration.class);
        install(new SpringModule(context));*/
    }
}
