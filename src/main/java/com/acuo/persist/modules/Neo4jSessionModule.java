package com.acuo.persist.modules;

import com.acuo.persist.core.Neo4jConfigFactory;
import com.acuo.persist.core.SingleNeo4jSessionFactory;
import com.google.inject.AbstractModule;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.SessionFactory;

public class Neo4jSessionModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Configuration.class).toProvider(Neo4jConfigFactory.class);
        bind(SessionFactory.class).toProvider(SingleNeo4jSessionFactory.class);
    }
}
