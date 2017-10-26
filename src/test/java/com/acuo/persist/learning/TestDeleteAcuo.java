package com.acuo.persist.learning;

import com.acuo.persist.services.AbstractService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.EnterpriseNeo4jRule;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.helpers.collection.Iterators;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.transaction.Transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestDeleteAcuo {

    private PersonService personGenericService;
    private CarService carGenericService;
    private Session session;
    private Transaction ogmTransaction;

    @Rule
    public Neo4jRule neo4jRule = new EnterpriseNeo4jRule()
            .withConfig("dbms.security.auth_enabled", "false");

    public static class PersonService extends AbstractService<Person, Long> {

        PersonService(Session session) {
            super(() -> session);
        }

        @Override
        public Class<Person> getEntityType() {
            return Person.class;
        }
    }

    public static class CarService extends AbstractService<Car, Long> {

        CarService(Session session) {
            super(() -> session);
        }

        @Override
        public Class<Car> getEntityType() {
            return Car.class;
        }
    }


    @Before
    public void setUp() {
        session = createSession();
        this.personGenericService = new PersonService(session);
        this.carGenericService = new CarService(session);
    }

    @Test
    public void testDelete() {

        startTransaction(session);
        Person pieter = new Person();
        Car mercedes = new Car();
        pieter.cars.add(mercedes);
        mercedes.owner = pieter;
        mercedes.type = "Mercedes";
        personGenericService.save(pieter);
        carGenericService.save(mercedes);
        endTransaciton();

        startTransaction(session);
        Person persistedPieter = Iterators.single(personGenericService.findAll().iterator());
        assertTrue(persistedPieter.cars.size() == 1);
        endTransaciton();

        startTransaction(session);
        persistedPieter = Iterators.single(personGenericService.findAll().iterator());
        Car persistedMercedes = Iterators.single(persistedPieter.cars.iterator());
        persistedPieter.cars.remove( persistedMercedes );
        carGenericService.delete(persistedMercedes.getId());
        endTransaciton();

        session.clear();
        startTransaction(session);
        Car bmw = new Car();
        bmw.type = "BMW";
        persistedPieter = Iterators.single(personGenericService.findAll().iterator());
        bmw.owner = persistedPieter;
        persistedPieter.cars.add( bmw );
        carGenericService.save(bmw);
        endTransaciton();

        startTransaction(session);
        persistedPieter = Iterators.single(personGenericService.findAll().iterator());
        assertThat(persistedPieter.cars).hasSize(1);
        assertEquals("BMW", Iterators.single(persistedPieter.cars.iterator()).type);
        endTransaciton();
    }

    private void startTransaction(Session neo4jSession) {
        ogmTransaction = neo4jSession.beginTransaction();
    }

    private void endTransaciton() {
        ogmTransaction.commit();
        ogmTransaction.close();
    }

    private Session createSession() {
        Configuration configuration = new Configuration();
        configuration
                .driverConfiguration()
                .setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver");
                //.setEncryptionLevel("NONE")
                //.setURI("bolt://localhost:5001");
        /*Configuration configuration = new Configuration.Builder()
                .uri("bolt://localhost:5001")
                .connectionPoolSize(150)
                .encryptionLevel("NONE")
                .build();*/
        return new SessionFactory(configuration, "com.acuo.persist.learning", "com.acuo.persist.entity").openSession();
    }
}