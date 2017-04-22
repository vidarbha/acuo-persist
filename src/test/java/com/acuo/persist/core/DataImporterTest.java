package com.acuo.persist.core;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.acuo.common.TestHelper.matchesArgRegex;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

public class DataImporterTest {

	private DataImporter importer;

	@Mock
	DataLoader loader;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		importer = new Neo4jDataImporter(loader, "graph-data", "develop","", "%s/cypher/%s.load");
	}

	@Test
	public void testCreateClient() {
		String query = "^LOAD CSV WITH HEADERS FROM.*AS line.*";

		importer.importFiles(null,"clients");

		verify(loader).loadData(argThat(matchesArgRegex(query)));
	}
}
