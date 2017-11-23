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
public class SingleNeo4jSessionFactory implements Provider<SessionFactory> {

    private final SessionFactory factory;

    @Inject
    public SingleNeo4jSessionFactory(Configuration configuration, Packages packages) {
        log.info("Creating a Neo4j Session Factory using config [{}] and packages [{}]", configuration, packages);
        this.factory = new SessionFactory(configuration, packages.value());
    }

    @Override
    public SessionFactory get() {
        return this.factory;
    }
}
