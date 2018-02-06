package com.acuo.persist.core;

import com.acuo.common.app.service.ServiceManagerModule;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.entity.Client;
import com.acuo.persist.modules.ConfigurationIntegrationTestModule;
import com.acuo.persist.modules.ImportServiceModule;
import com.acuo.persist.modules.InProcessNeo4jServerModule;
import com.acuo.persist.modules.RepositoryTestModule;
import com.acuo.persist.services.ClientService;
import com.google.common.util.concurrent.ServiceManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
		ConfigurationIntegrationTestModule.class,
		ImportServiceModule.class,
		RepositoryTestModule.class,
		ServiceManagerModule.class,
		InProcessNeo4jServerModule.class
})
public class SessionIntegrationTest {

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

        importer.withBranch("develop").reload("ACUO");
	}

	@Test
	public void testFindByClientId() {
		Client client = clientService.find("999");
		assertNotNull(client.getFirmId());
	}

	@Test
	public void testFindAll() {
		Iterable<Client> all = clientService.findAll();
		for (Client client : all) {
			assertNotNull(client.getFirmId());
		}
	}

	@After
	public void teardown() {
		importer.deleteAll();
	}
}
