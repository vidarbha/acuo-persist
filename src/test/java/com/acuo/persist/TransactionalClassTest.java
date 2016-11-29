package com.acuo.persist;

import com.google.inject.persist.Transactional;
import org.junit.Test;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author cgdecker@gmail.com (Colin Decker)
 */
public class TransactionalClassTest extends AbstractPersistTest {

  @Test public void transactionsStartAndEndAroundMethods() {
    getUnitOfWork().begin();
    assertTransactionNotActive();
    injector.getInstance(TransactionChecker.class).checkForActiveTransaction(this);
    assertTransactionNotActive();
    getUnitOfWork().end();
  }

  @Test public void savingAndRetrieving() {
    NonThrowingRepository repository = injector.getInstance(NonThrowingRepository.class);

    getUnitOfWork().begin();

    assertTrue("sanity check failed", getFoos().isEmpty());
    repository.addFoo(new Foo(1, "abc"));

    Collection<Foo> savedFoos = getFoos();
    assertEquals(1, savedFoos.size());
    assertTrue(savedFoos.contains(new Foo(1, "abc")));

    getUnitOfWork().end();

    getUnitOfWork().begin();

    savedFoos = getFoos();
    assertEquals(1, savedFoos.size());
    assertTrue(savedFoos.contains(new Foo(1, "abc")));

    getUnitOfWork().end();
  }

  @Test public void runtimeExceptionCausesRollback() {
    testRollbackOccurs(RuntimeExceptionThrowingRepository.class, IllegalStateException.class);
  }

  @Test public void checkedExceptionCausesRollback() throws SQLException {
    testRollbackOccurs(CheckedExceptionThrowingRepository.class, FileNotFoundException.class);
  }

  @Test public void ignoredCheckedExceptionDoesNotCauseRollback() throws SQLException {
    testRollbackDoesNotOccur(IgnoredCheckedExceptionThrowingRepository.class, FileNotFoundException.class);
  }

  @Test public void ignoredRuntimeExceptionDoesNotCauseRollback() throws SQLException {
    testRollbackDoesNotOccur(IgnoredRuntimeExceptionThrowingRepository.class, IllegalStateException.class);
  }

  @Transactional
  public static class TransactionChecker {
    @Inject private Provider<Session> connectionProvider;

    public void checkForActiveTransaction(TransactionalClassTest test) {
      test.assertTransactionActive();
    }
  }

  @Transactional
  public static class NonThrowingRepository {
    private final Provider<Session> connectionProvider;

    @Inject public NonThrowingRepository(Provider<Session> connectionProvider) {
      this.connectionProvider = connectionProvider;
    }

    public void addFoo(Foo foo) {
      add(connectionProvider.get(), foo);
    }
  }

  @Transactional
  public static class RuntimeExceptionThrowingRepository implements FooAdder<IllegalStateException> {
    private final Provider<Session> connectionProvider;

    @Inject public RuntimeExceptionThrowingRepository(Provider<Session> connectionProvider) {
      this.connectionProvider = connectionProvider;
    }

    public void addFoo(Foo foo) {
      add(connectionProvider.get(), foo);
      throw new IllegalStateException();
    }
  }

  @Transactional(rollbackOn = IOException.class)
  public static class CheckedExceptionThrowingRepository implements FooAdder<FileNotFoundException> {
    private final Provider<Session> connectionProvider;

    @Inject public CheckedExceptionThrowingRepository(Provider<Session> connectionProvider) {
      this.connectionProvider = connectionProvider;
    }

    public void addFoo(Foo foo) throws FileNotFoundException {
      add(connectionProvider.get(), foo);
      throw new FileNotFoundException();
    }
  }

  @Transactional(rollbackOn = IOException.class, ignore = FileNotFoundException.class)
  public static class IgnoredCheckedExceptionThrowingRepository implements FooAdder<FileNotFoundException> {
    private final Provider<Session> connectionProvider;

    @Inject public IgnoredCheckedExceptionThrowingRepository(Provider<Session> connectionProvider) {
      this.connectionProvider = connectionProvider;
    }

    public void addFoo(Foo foo) throws FileNotFoundException {
      add(connectionProvider.get(), foo);
      throw new FileNotFoundException();
    }
  }

  @Transactional(ignore = IllegalStateException.class)
  public static class IgnoredRuntimeExceptionThrowingRepository implements FooAdder<IllegalStateException> {
    private final Provider<Session> connectionProvider;

    @Inject public IgnoredRuntimeExceptionThrowingRepository(Provider<Session> connectionProvider) {
      this.connectionProvider = connectionProvider;
    }

    public void addFoo(Foo foo) {
      add(connectionProvider.get(), foo);
      throw new IllegalStateException();
    }
  }

  private static void add(Session session, Foo foo) {
    session.save(foo);
  }
}