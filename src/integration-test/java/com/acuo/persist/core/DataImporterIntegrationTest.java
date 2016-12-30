package com.acuo.persist.core;

import com.acuo.common.app.ServiceManagerModule;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.entity.Client;
import com.acuo.persist.modules.*;
import com.acuo.persist.services.ClientService;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ServiceManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.ogm.testutil.TestServer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import static com.acuo.persist.configuration.PropertiesHelper.ACUO_CYPHER_DIR_TEMPLATE;
import static com.acuo.persist.configuration.PropertiesHelper.ACUO_DATA_DIR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Ignore
@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationIntegrationTestModule.class,
        Neo4jPersistModule.class,
        DataLoaderModule.class,
        RepositoryModule.class,
        ServiceManagerModule.class,
        Neo4jIntegrationTestModule.class})
public class DataImporterIntegrationTest {

    private DataImporter importer;
    private CypherExecutor executor;

    @Inject
    ServiceManager serviceManager;

    @Inject
    Provider<TestServer> serverProvider;

    @Inject
    @Named(ACUO_DATA_DIR)
    private String workingDirectory;

    @Inject
    @Named(ACUO_CYPHER_DIR_TEMPLATE)
    private String directoryTemplate;

    @Inject
    DataLoader dataLoader;

    @Inject
    ClientService clientService;

    @Before
    public void setup() {
        if (!serviceManager.isHealthy()) {
            serviceManager.startAsync().awaitHealthy();
        }
        importer = new Neo4jDataImporter(dataLoader, workingDirectory, workingDirectory, directoryTemplate);
        executor = new EmbeddedCypherExecutor(serverProvider.get().getGraphDatabaseService());
    }

    @Test
    public void testImportClients() {
        importer.importFiles("clients");

        Iterable<Client> clients = clientService.findAll();

        assertEquals(1, Iterables.size(clients));

        for (Client client : clients) {
            assertNotNull(client.getFirmId());
            assertNotNull(client.getName());
        }
    }

    @After
    public void teardown() {
        dataLoader.purgeDatabase();
    }
}
