package com.acuo.persist.core;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CypherFileSpliterTest {
	
	@Test
	public void testNewBuilder() {
		CypherFileSpliter spliter = CypherFileSpliter.newBuilder("/").build();
				
		assertNotNull("Spliter shouldnt be null", spliter);
	}

	@Test
	public void testSplitByLine() {
		CypherFileSpliter spliter = CypherFileSpliter.newBuilder("./src/test/resources").build();
				
		List<String> lines = spliter.splitByLine("spliter-test.txt");
		
		assertNotNull(lines);
		assertEquals(3, lines.size());
	}

	@Test
	public void testSplitByLineFromNonExistingWorkingDirectory() {
		CypherFileSpliter spliter = CypherFileSpliter.newBuilder("./notfound").build();
				
		List<String> lines = spliter.splitByLine("spliter-test.txt");
		
		assertNotNull(lines);
		assertEquals(0, lines.size());
	}
	
	@Test
	public void testSplitByLineFromNonExistingFile() {
		CypherFileSpliter spliter = CypherFileSpliter.newBuilder("./src/test/resources").build();
		
		List<String> lines = spliter.splitByLine("notfound.txt");
		
		assertNotNull(lines);
		assertEquals(0, lines.size());
	}
	
	@Test
	public void testSplitByDefaultDelimiter() {
		CypherFileSpliter spliter = CypherFileSpliter.newBuilder(".").build();

		List<String> lines = spliter.splitByDefaultDelimiter("file://spliter-test-with-default-delim.txt");
		
		assertNotNull(lines);
		assertEquals(3, lines.size());
		for (String line : lines) {
			assertFalse(line.contains("\r"));
			assertFalse(line.contains("\n"));
		}
		
	}
	
	@Test
	public void testSplitByColonDelimiterPassedAsDefault() {
		CypherFileSpliter spliter = CypherFileSpliter.newBuilder(".")
													 .withDelimiter(":")
													 .build();
		
		List<String> lines = spliter.splitByDefaultDelimiter("spliter-test-with-colon-delim.txt");
		
		assertNotNull(lines);
		assertEquals(3, lines.size());
		for (String line : lines) {
			assertFalse(line.contains("\r"));
			assertFalse(line.contains("\n"));
		}
		
	}
	
	@Test
	public void testSplitByColonDelimiterPassedAsArgument() {
		CypherFileSpliter spliter = CypherFileSpliter.newBuilder(".").build();
		
		List<String> lines = spliter.splitByDelimiter("spliter-test-with-colon-delim.txt", ":");
		
		assertNotNull(lines);
		assertEquals(3, lines.size());
		for (String line : lines) {
			assertFalse(line.contains("\r"));
			assertFalse(line.contains("\n"));
		}
		
	}
}
