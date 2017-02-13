package com.acuo.persist.spring;

import com.acuo.persist.modules.Neo4jPersistModule;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.SessionFactory;

/*@Configuration
@ComponentScan(basePackages = "com.acuo.core.spring")
@EnableNeo4jRepositories(basePackages = "com.acuo.core.spring")
@EnableTransactionManagement
@EnableGuiceModules*/
@Slf4j
public class MyConfiguration extends AbstractModule {

    //@Autowired
    private Injector injector;

    //@Bean
    public Neo4jPersistModule neo4jPersistModule() {
        return new Neo4jPersistModule();
    }

    //@Bean
    public SessionFactory sessionFactory() {
        log.info("{}",injector);
        return null;
    }

	//@Bean
    /*public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(sessionFactory());
	}*/

    @Override
    protected void configure() {
        log.info("in configure {}",injector);
    }
}