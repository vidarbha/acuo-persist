package com.acuo.persist.learning;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class Neo4jTest {

    @Rule
    public Neo4jRule neo4jRule;

    private Session session;

    @Before
    public void prepareSession() {
        session = createSession();
    }

    @Test
    public void testSession() {
        assertThat(session).isNotNull();
    }

    private Session createSession() {
        Configuration configuration = new Configuration();
        configuration
                .driverConfiguration()
                .setDriverClassName("org.neo4j.ogm.drivers.bolt.driver.BoltDriver")
                .setEncryptionLevel( "NONE" )
                .setURI("bolt://localhost:5001");
        return new SessionFactory( configuration, "com.acuo.persist.entity").openSession();
    }
}
