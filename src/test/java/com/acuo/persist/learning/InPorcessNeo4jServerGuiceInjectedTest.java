package com.acuo.persist.learning;

import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.acuo.persist.modules.ImportTestServiceModule;
import com.acuo.persist.modules.InProcessNeo4jServerModule;
import com.acuo.persist.modules.RepositoryModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.ogm.session.SessionFactory;

import javax.inject.Inject;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationTestModule.class,
        InProcessNeo4jServerModule.class,
        ImportTestServiceModule.class,
        RepositoryModule.class})
public class InPorcessNeo4jServerGuiceInjectedTest {

    @Inject
    private SessionFactory factory = null;

    @Test
    public void testSessionFactoryNotNull() {
        assert factory != null;
    }
}
