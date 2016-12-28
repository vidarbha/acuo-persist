package com.acuo.persist.spring;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.guice.module.SpringModule;

import javax.inject.Inject;

public class SpringGuiceModule extends AbstractModule {

    @Inject
    Injector injector;

    @Override
    protected void configure() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfiguration.class);
        //install(new SpringModule(context));
        install(injector.getInstance(SpringModule.class));
    }
}
