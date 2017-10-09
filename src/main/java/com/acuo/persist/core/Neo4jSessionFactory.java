package com.acuo.persist.core;

import com.acuo.persist.configuration.PropertiesHelper;
import com.acuo.persist.modules.Packages;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.authentication.UsernamePasswordCredentials;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.SessionFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
@Slf4j
public class Neo4jSessionFactory implements Provider<SessionFactory> {

    private final String[] persistencePackages;
    private final Configuration configuration;

    @Inject
    public Neo4jSessionFactory(@Named(PropertiesHelper.NEO4J_OGM_DRIVER) String driver,
                        @Named(PropertiesHelper.NEO4J_OGM_URL) String url,
                        @Named(PropertiesHelper.NEO4J_OGM_USERNAME) String userName,
                        @Named(PropertiesHelper.NEO4J_OGM_PASSWORD) String password,
                        Packages packages) {
        log.info("Creating a Neo4j Session Factory using driver [{}] and url[{}]", driver, url);
        if ("".equals(url)) url = null;
        this.persistencePackages = packages.value();
        this.configuration = new Configuration();
        this.configuration.autoIndexConfiguration().setAutoIndex("assert");
        this.configuration.driverConfiguration().setDriverClassName(driver).setURI(url)
                .setConnectionPoolSize(150)
                .setEncryptionLevel("NONE")
                .setCredentials(new UsernamePasswordCredentials(userName, password));
        /*this.configuration = new Configuration.Builder()
                .autoIndex("assert")
                .uri(url)
                .connectionPoolSize(150)
                .encryptionLevel("NONE")
                .credentials(userName, password)
                .build();*/
    }

    @Override
    public SessionFactory get() {
        return new SessionFactory(configuration, persistencePackages);
    }
}
