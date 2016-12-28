package com.acuo.persist.spring;

import com.acuo.persist.core.Neo4jSessionFactory;
import com.google.inject.Injector;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.guice.annotation.EnableGuiceModules;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Provider;

@Configuration
@ComponentScan(basePackages = "com.acuo.persist.spring")
@EnableNeo4jRepositories(basePackages = "com.acuo.persist.spring")
@EnableTransactionManagement
@EnableGuiceModules
public class MyConfiguration {

    @Autowired
    private Injector injector;

    @Bean
    public SessionFactory sessionFactory() {
        Provider<SessionFactory>  provider = injector.getInstance(Neo4jSessionFactory.class);
        return provider.get();
    }

	@Bean
    public Neo4jTransactionManager transactionManager() {
		return new Neo4jTransactionManager(sessionFactory());
	}
}