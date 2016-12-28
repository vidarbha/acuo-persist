package com.acuo.persist.spring;

import com.acuo.persist.core.Neo4jSessionFactory;
import com.google.inject.Injector;
import org.neo4j.ogm.session.SessionFactory;

import javax.inject.Provider;

/*@Configuration
@ComponentScan(basePackages = "com.acuo.persist.spring")
@EnableNeo4jRepositories(basePackages = "com.acuo.persist.spring")
@EnableTransactionManagement
@EnableGuiceModules*/
public class MyConfiguration {

    //@Autowired
    private Injector injector;

    //@Bean
    public SessionFactory sessionFactory() {
        Provider<SessionFactory>  provider = injector.getInstance(Neo4jSessionFactory.class);
        return provider.get();
    }

	/*@Bean
    public Neo4jTransactionManager transactionManager() {
		return new Neo4jTransactionManager(sessionFactory());
	}*/
}