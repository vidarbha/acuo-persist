package com.acuo.persist.modules;

import com.acuo.persist.core.SingleNeo4jSessionFactory;
import com.google.inject.AbstractModule;
import org.neo4j.backup.OnlineBackupSettings;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilder;
import org.neo4j.harness.TestServerBuilders;
import org.neo4j.kernel.configuration.Settings;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.SessionFactory;

public class InProcessNeo4jServerModule extends AbstractModule {

    private static ServerControls controls;
    static {
        TestServerBuilder builder = TestServerBuilders.newInProcessBuilder();
        builder.withConfig(OnlineBackupSettings.online_backup_enabled, Settings.FALSE);
        controls = builder.newServer();
    }

    @Override
    protected void configure() {
        Configuration configuration = new Configuration.Builder()
                .uri(controls.boltURI().toString())
                .connectionPoolSize(150)
                .encryptionLevel("NONE")
                .build();

        bind(Configuration.class).toInstance(configuration);
        bind(SessionFactory.class).toProvider(SingleNeo4jSessionFactory.class);
    }


//
//    @Singleton
//    static class CloseableTestServer {
//
//        private final TestServerBuilder builder = EnterpriseTestServerBuilders.newInProcessBuilder();
//        private ServerControls serverControls = null;
//
//        public CloseableTestServer() {
//            builder.withConfig(OnlineBackupSettings.online_backup_enabled, Settings.FALSE);
//        }
//
//        @PostConstruct
//        public void start() {
//            serverControls = builder.newServer();
//        }
//
//        @PreDestroy
//        public void stop() {
//            if (serverControls != null) {
//                serverControls.close();
//            }
//        }
//
//        public ServerControls controls() {
//            return serverControls;
//        }
//    }
}
