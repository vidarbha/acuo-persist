package com.acuo.persist.core;

import com.acuo.persist.modules.Packages;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.SessionFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
@Slf4j
public class Neo4jSessionFactory implements Provider<SessionFactory> {

    private final String[] persistencePackages;
    private final Configuration configuration;

    @Inject
    public Neo4jSessionFactory(Configuration configuration, Packages packages) {
        this.persistencePackages = packages.value();
        this.configuration = configuration;
    }

    @Override
    public SessionFactory get() {
        log.info("Creating a Neo4j Session Factory using config [{}] and packages [{}]", configuration, persistencePackages);
        return new SessionFactory(configuration, persistencePackages);
    }
}
