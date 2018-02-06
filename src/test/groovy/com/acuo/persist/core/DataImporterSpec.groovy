package com.acuo.persist.core

import org.mockito.MockitoAnnotations
import spock.lang.Specification

class DataImporterSpec extends Specification {

	private DataImporter importer

	DataLoader loader = Mock()

	void setup() {
		MockitoAnnotations.initMocks(this)

		importer = new Neo4jDataImporter(loader, "graph-data", "develop", "","file1, file2", "%s/cypher/%s.load", "ACUO")
	}

	void "load client data file"() {
		given:
		def regex = /LOAD CSV WITH HEADERS FROM.*AS line.*/

		when:
		importer.load("ACUO", "clients")

		then:
		1 * loader.loadData(_ as String[]) >> { arguments ->
			arguments.forEach {
				it.any{
					assert it =~ regex
				}
			}
		}
	}
}
