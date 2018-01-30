package com.acuo.persist.core;

import com.acuo.common.app.service.ServiceManagerModule;
import com.acuo.common.util.GuiceJUnitRunner;
import com.acuo.persist.entity.Client;
import com.acuo.persist.modules.ConfigurationIntegrationTestModule;
import com.acuo.persist.modules.Neo4jIntegrationTestModule;
import com.acuo.persist.modules.RepositoryTestModule;
import com.acuo.persist.services.ClientService;
import com.google.common.util.concurrent.ServiceManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.inject.Named;

import static com.acuo.persist.configuration.PropertiesHelper.*;
import static org.junit.Assert.assertNotNull;

@Ignore
@RunWith(GuiceJUnitRunner.class)
@GuiceJUnitRunner.GuiceModules({
		ConfigurationIntegrationTestModule.class,
		RepositoryTestModule.class,
		ServiceManagerModule.class,
		Neo4jIntegrationTestModule.class})
public class SessionIntegrationTest {

	@Inject
    private ServiceManager serviceManager;

	@Inject
	@Named(ACUO_DATA_BRANCH)
	private String dataBranch;

	@Inject
	@Named(ACUO_DATA_DIR)
	private String workingDirectory;

	@Inject
	@Named(ACUO_CYPHER_DIR_TEMPLATE)
	private String directoryTemplate;

	@Inject
	@Named(ACUO_DATA_IMPORT_FILES)
	private String dataImportFiles;

	@Inject
    private DataLoader dataLoader;

	@Inject
    private ClientService clientService;

	@Before
	public void setup() {
		if (!serviceManager.isHealthy()) {
			serviceManager.startAsync().awaitHealthy();
		}

        DataImporter importer = new Neo4jDataImporter(dataLoader, workingDirectory, dataBranch, dataImportFiles, workingDirectory, directoryTemplate);
        importer.importFiles("develop", "ACUO", importer.filesToImport());
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
		dataLoader.purgeDatabase();
	}
}
