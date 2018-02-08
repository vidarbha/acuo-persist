package com.acuo.persist.core;

import com.acuo.persist.configuration.PropertiesHelper;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.config.Configuration;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class Neo4jConfigFactory implements Provider<Configuration> {

    private final Configuration configuration;

    @Inject
    public Neo4jConfigFactory(@Named(PropertiesHelper.NEO4J_OGM_DRIVER) String driver,
                               @Named(PropertiesHelper.NEO4J_OGM_URL) String url,
                               @Named(PropertiesHelper.NEO4J_OGM_USERNAME) String userName,
                               @Named(PropertiesHelper.NEO4J_OGM_PASSWORD) String password) {
        log.info("Creating a Neo4j OGM Configuration using driver [{}] and url[{}]", driver, url);
        if ("".equals(url)) url = null;
        this.configuration = new Configuration.Builder()
                .autoIndex("none")
                .uri(url)
                .connectionPoolSize(150)
                .encryptionLevel("NONE")
                .credentials(userName, password)
                .build();
    }

    @Override
    public Configuration get() {
        return configuration;
    }
}
