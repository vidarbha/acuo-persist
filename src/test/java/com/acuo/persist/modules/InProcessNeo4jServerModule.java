package com.acuo.persist.modules;

import com.acuo.common.util.AnnotationsModule;
import com.acuo.persist.core.SingleNeo4jSessionFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.neo4j.backup.OnlineBackupSettings;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;
import org.neo4j.kernel.configuration.Settings;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.SessionFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

public class InProcessNeo4jServerModule extends AbstractModule {

    @Override
    protected void configure() {

        install(new AnnotationsModule());

        bind(SessionFactory.class).toProvider(SingleNeo4jSessionFactory.class);
    }

    @Provides
    Configuration configuration(CloseableTestServer server) {
        Configuration configuration = new Configuration.Builder()
                .uri(server.controls().boltURI().toString())
                .connectionPoolSize(150)
                .encryptionLevel("NONE")
                .build();
        return configuration;
    }

    @Provides
    ServerControls serverControls(CloseableTestServer server) {
        return server.serverControls;
    }

    @Singleton
    static class CloseableTestServer {

        private ServerControls serverControls = null;

        @PostConstruct
        public void start() {
            serverControls = TestServerBuilders.newInProcessBuilder()
                            .withConfig(OnlineBackupSettings.online_backup_enabled, Settings.FALSE)
                            .newServer();
        }

        @PreDestroy
        public void stop() {
            if (serverControls != null) {
                serverControls.close();
            }
        }

        public ServerControls controls() {
            return serverControls;
        }
    }
}
