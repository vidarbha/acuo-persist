package com.acuo.persist.transaction;

import com.acuo.persist.entity.Foo;
import com.acuo.persist.modules.Neo4jPersistModule;
import com.acuo.persist.core.Neo4jPersistService;
import com.acuo.persist.modules.ConfigurationTestModule;
import com.google.common.base.Throwables;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import org.junit.After;
import org.junit.Before;
import org.neo4j.ogm.session.Session;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public abstract class AbstractPersistTest {

  protected Injector injector;

  @Before
  public void setUp() {
    injector = Guice.createInjector(new ConfigurationTestModule(), new Neo4jPersistModule());
    injector.getInstance(PersistService.class).start();
  }

  @After
  public void tearDown() {
    getUnitOfWork().begin();
    injector.getInstance(Session.class).purgeDatabase();
    getUnitOfWork().end();
    injector.getInstance(PersistService.class).stop();
  }

  protected PersistService getPersistService() {
    return injector.getInstance(PersistService.class);
  }

  protected UnitOfWork getUnitOfWork() {
    return injector.getInstance(UnitOfWork.class);
  }

  protected void assertTransactionActive() {
    assertTransaction(true);
  }

  protected void assertTransactionNotActive() {
    assertTransaction(false);
  }

  private void assertTransaction(boolean active) {
    String message = active ? "transaction not active when it should have been" :
        "transaction active when it should not have been";
    assertEquals(message, active, injector.getInstance(Session.class).getTransaction() != null);
  }

  protected Collection<Foo> getFoos() {
    return injector.getInstance(FooGetter.class).getFoos();
  }

  protected <X extends Exception, T extends FooAdder<X>> void testRollbackOccurs(Class<T> type,
                                                                                 Class<X> exceptionType) {
    testRollback(true, type, exceptionType);
  }

  protected <X extends Exception, T extends FooAdder<X>> void testRollbackDoesNotOccur(
      Class<T> type, Class<X> exceptionType) {
    testRollback(false, type, exceptionType);
  }

  protected <X extends Exception, T extends FooAdder<X>> void testRollback(boolean expected,
                                                                           Class<T> type,
                                                                           Class<X> exceptionType) {
    getUnitOfWork().begin();

    T repository = injector.getInstance(type);

    try {
      repository.addFoo(new Foo(1, "bar"));
    } catch (Exception e) {
      if (!exceptionType.isInstance(e))
        throw Throwables.propagate(e);
    }

    String message = expected ? "got a result, no rollback occurred" :
        "didn't get a result, a rollback occurred";
    assertEquals(message, expected, getFoos().isEmpty());

    getUnitOfWork().end();
  }

  protected void assertUnitOfWorkNotActive() {
    assertFalse("unit of work active when it should not have been",
        injector.getInstance(Neo4jPersistService.class).isWorking());
  }
}