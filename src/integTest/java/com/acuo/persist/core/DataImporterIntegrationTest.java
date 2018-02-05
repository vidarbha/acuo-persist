package com.acuo.persist.core;

import com.acuo.common.app.service.ServiceManagerModule;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.entity.Client;
import com.acuo.persist.modules.ConfigurationIntegrationTestModule;
import com.acuo.persist.modules.ImportServiceModule;
import com.acuo.persist.modules.InProcessNeo4jServerModule;
import com.acuo.persist.modules.RepositoryTestModule;
import com.acuo.persist.services.ClientService;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ServiceManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
        ConfigurationIntegrationTestModule.class,
        ImportServiceModule.class,
        RepositoryTestModule.class,
        ServiceManagerModule.class,
        InProcessNeo4jServerModule.class
})
public class DataImporterIntegrationTest {

    @Inject
    private ServiceManager serviceManager;

    @Inject
    private DataImporter importer = null;

    @Inject
    private ClientService clientService;

    @Before
    public void setup() {
        if (!serviceManager.isHealthy()) {
            serviceManager.startAsync().awaitHealthy();
        }
    }

    @Test
    public void testImportClients() {
        importer.withBranch("develop").load( "ACUO", "firms");

        Iterable<Client> clients = clientService.findAll();

        assertEquals(1, Iterables.size(clients));

        for (Client client : clients) {
            assertNotNull(client.getFirmId());
            assertNotNull(client.getName());
        }
    }

    @After
    public void teardown() {
        importer.deleteAll();
    }
}