package com.acuo.persist.modules;

import com.acuo.common.app.AppId;
import com.acuo.common.app.Configuration;
import com.acuo.common.app.Environment;
import com.acuo.persist.configuration.PropertiesModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ConfigurationTestModule extends AbstractModule {

	@Override
	protected void configure() {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(Configuration.class).toInstance(Configuration.builder(AppId.of("persist")).with(Environment.TEST).build());
			}
		});
		install(injector.getInstance(PropertiesModule.class));
	}

}
