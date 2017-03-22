package com.acuo.persist.transaction;

import com.google.inject.ProvisionException;
import org.junit.Test;
import org.neo4j.ogm.session.Session;

import static org.junit.Assert.*;

public class UnitOfWorkTest extends AbstractPersistTest {

    @Test
    public void differentConnectionsForDifferentUnitsOfWork() {
        getUnitOfWork().begin();
        Session firstConn = injector.getInstance(Session.class);
        assertEquals(firstConn, injector.getInstance(Session.class));
        getUnitOfWork().end();

        getUnitOfWork().begin();
        Session secondConn = injector.getInstance(Session.class);
        assertFalse(firstConn.equals(secondConn));
        assertEquals(secondConn, injector.getInstance(Session.class));
        getUnitOfWork().end();
    }

    @Test
    public void illegalToRetrieveConnectionOutsideUnitOfWork() {
        try {
            injector.getInstance(Session.class);
            fail();
        } catch (ProvisionException e) {
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }

    @Test
    public void unitsOfWorkNestCorrectly() {
        getUnitOfWork().begin();
        Session conn = injector.getInstance(Session.class);
        assertFalse("conn is not open", conn == null);

        getUnitOfWork().begin();
        getUnitOfWork().begin();

        assertEquals(conn, injector.getInstance(Session.class));
        assertFalse("conn is not open", conn == null);

        getUnitOfWork().end();

        assertEquals(conn, injector.getInstance(Session.class));
        assertFalse("conn is not open", conn == null);

        getUnitOfWork().begin();
        getUnitOfWork().end();

        getUnitOfWork().end();

        assertEquals(conn, injector.getInstance(Session.class));
        assertFalse("conn is not open", conn == null);

        getUnitOfWork().end();

        try {
            injector.getInstance(Session.class);
            fail("unit of work did not end when expected");
        } catch (ProvisionException expected) {
            assertEquals(IllegalStateException.class, expected.getCause().getClass());
        }
    }

    @Test
    public void retrievingConnectionOutsideUnitOfWorkIsIllegal() {
        try {
            injector.getInstance(Session.class);
            fail();
        } catch (ProvisionException e) {
            assertEquals(IllegalStateException.class, e.getCause().getClass());
        }
    }
}