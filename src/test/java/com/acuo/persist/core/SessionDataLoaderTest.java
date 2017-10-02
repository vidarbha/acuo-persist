package com.acuo.persist.core;

import com.google.inject.Provider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.neo4j.ogm.session.Session;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class SessionDataLoaderTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private Provider<Session> sessionProvider;

	@Mock
	private Session session;

	private DataLoader loader;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		when(sessionProvider.get()).thenReturn(session);

		loader = new SessionDataLoader(sessionProvider, "graph-data");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLoadDataWithEmptyQuery() {
		loader.loadData("");

		verify(session, times(0)).query(anyString(), anyMap());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLoadDataWithNullQuery() {
		loader.loadData(null);

		verify(session, times(0)).query(anyString(), anyMap());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLoadDataWithDummyQuery() {
		loader.loadData("dummy");

		verify(session, times(1)).query(eq("dummy"), anyMap());
	}

	@Test
	public void testDeleteAll() {
		loader.purgeDatabase();

		verify(session, times(1)).purgeDatabase();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateConstraints() {
		loader.createConstraints();

		verify(session, times(0)).query(anyString(), anyMap());
	}

}
