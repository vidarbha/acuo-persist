package com.acuo.persist.core

import spock.lang.Specification

class CypherFileSpliterSpec extends Specification {
	
	void "call new builder"() {
		given:
		CypherFileSpliter spliter = CypherFileSpliter.newBuilder("/").build()

		expect:
		spliter != null
	}

	void "split by line"() {
		given:
		CypherFileSpliter spliter = CypherFileSpliter.newBuilder("./src/test/resources").build()
		List<String> lines = spliter.splitByLine("spliter-test.txt")

        expect:
		lines != null
		lines.size() == 3
	}

	void "split by line from non existing working directory"() {
		given:
		CypherFileSpliter spliter = CypherFileSpliter.newBuilder("./notfound").build()
		List<String> lines = spliter.splitByLine("spliter-test.txt")

        expect:
		lines != null
		lines.size() == 0
	}
	
	void "split by line from non existing file"() {
		given:
		CypherFileSpliter spliter = CypherFileSpliter.newBuilder("./src/test/resources").build()
		List<String> lines = spliter.splitByLine("notfound.txt")

        expect:
		lines != null
		lines.size() == 0
	}
	
	void "split by default delimiter"() {
		given:
		CypherFileSpliter spliter = CypherFileSpliter.newBuilder(".").build()
		List<String> lines = spliter.splitByDefaultDelimiter("./cypher/spliter-test-with-default-delim.txt")

        expect:
		lines != null
		lines.size() == 3
		for (String line : lines) {
			assert !line.contains("\r")
			assert !line.contains("\n")
		}
		
	}
	
	void "split by colon delimiter passed as default"() {
		given:
		CypherFileSpliter spliter = CypherFileSpliter.newBuilder(".")
													 .withDelimiter(":")
													 .build()
		List<String> lines = spliter.splitByDefaultDelimiter("./cypher/spliter-test-with-colon-delim.txt")

        expect:
        lines != null
		lines.size() == 3
		for (String line : lines) {
			assert !line.contains("\r")
			assert !line.contains("\n")
		}
		
	}
	
	void "split by dolon delimiter passed as argument"() {
		given:
		CypherFileSpliter spliter = CypherFileSpliter.newBuilder(".").build()
		List<String> lines = spliter.splitByDelimiter("./cypher/spliter-test-with-colon-delim.txt", ":")

        expect:
        lines != null
		lines.size() == 3
		for (String line : lines) {
			assert !line.contains("\r")
			assert !line.contains("\n")
		}
		
	}
}