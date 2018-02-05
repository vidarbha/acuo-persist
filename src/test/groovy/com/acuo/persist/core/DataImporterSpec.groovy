package com.acuo.persist.core

import org.mockito.MockitoAnnotations
import spock.lang.Specification

class DataImporterSpec extends Specification {

	private DataImporter importer

	DataLoader loader = Mock()

	void setup() {
		MockitoAnnotations.initMocks(this)

		importer = new Neo4jDataImporter(loader, "graph-data", "develop", "","file1, file2", "%s/cypher/%s.load")
	}

	void "load client data file"() {
		given:
		def regex = /LOAD CSV WITH HEADERS FROM.*AS line.*/

		when:
		importer.importFiles(null, "ACUO", "clients")

		then:
		1 * loader.loadData(_ as String[]) >> { arguments ->
			arguments.forEach {
				it.any{
					assert it =~ regex
				}
			}
		}
	}

	void "getting files to import"() {
		given:
		final String[] values = importer.filesToImport()

		expect:
		values != null
		values.size() == 2
	}
}
