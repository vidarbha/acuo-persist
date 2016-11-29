package com.acuo.persist.configuration;

import com.acuo.common.app.Configuration;
import com.google.inject.AbstractModule;

import javax.inject.Inject;

public class PropertiesModule extends AbstractModule {

    @Inject
    private Configuration configuration;

    @Override
    protected void configure() {
        PropertiesHelper helper = PropertiesHelper.of(configuration);
        install(new com.smokejumperit.guice.properties.PropertiesModule(helper.getDefaultProperties(),
                helper.getOverrides()));
    }

}