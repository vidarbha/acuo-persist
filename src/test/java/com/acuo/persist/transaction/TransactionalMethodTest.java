package com.acuo.persist.transaction;

import com.acuo.persist.entity.Foo;
import com.google.inject.persist.Transactional;
import org.junit.Test;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TransactionalMethodTest extends AbstractPersistTest {

    @Test
    public void runtimeExceptionCausesRollback() {
        testRollbackOccurs(RuntimeExceptionThrowingRepository.class, IllegalStateException.class);
    }

    @Test
    public void checkedExceptionCausesRollback() {
        testRollbackOccurs(CheckedExceptionThrowingRepository.class, FileNotFoundException.class);
    }

    @Test
    public void ignoredCheckedExceptionDoesNotCauseRollback() {
        testRollbackDoesNotOccur(IgnoredCheckedExceptionThrowingRepository.class, FileNotFoundException.class);
    }

    @Test
    public void ignoredRuntimeExceptionDoesNotCauseRollback() {
        testRollbackDoesNotOccur(IgnoredRuntimeExceptionThrowingRepository.class, IllegalStateException.class);
    }

    public static class TransactionChecker {
        @Inject
        private Provider<Session> connectionProvider;

        @Transactional
        public void checkForActiveTransaction(AbstractPersistTest test) {
            test.assertTransactionActive();
        }
    }

    public static class NonThrowingRepository {
        private final Provider<Session> connectionProvider;

        @Inject
        public NonThrowingRepository(Provider<Session> connectionProvider) {
            this.connectionProvider = connectionProvider;
        }

        @Transactional
        public void addFoo(Foo foo) {
            add(connectionProvider.get(), foo);
        }
    }

    public static class RuntimeExceptionThrowingRepository implements FooAdder<IllegalStateException> {
        private final Provider<Session> connectionProvider;

        @Inject
        public RuntimeExceptionThrowingRepository(Provider<Session> connectionProvider) {
            this.connectionProvider = connectionProvider;
        }

        @Transactional
        public void addFoo(Foo foo) {
            add(connectionProvider.get(), foo);
            throw new IllegalStateException();
        }
    }

    public static class CheckedExceptionThrowingRepository implements FooAdder<FileNotFoundException> {
        private final Provider<Session> connectionProvider;

        @Inject
        public CheckedExceptionThrowingRepository(Provider<Session> connectionProvider) {
            this.connectionProvider = connectionProvider;
        }

        @Transactional(rollbackOn = IOException.class)
        public void addFoo(Foo foo) throws FileNotFoundException {
            add(connectionProvider.get(), foo);
            throw new FileNotFoundException();
        }
    }

    public static class IgnoredCheckedExceptionThrowingRepository implements FooAdder<FileNotFoundException> {
        private final Provider<Session> connectionProvider;

        @Inject
        public IgnoredCheckedExceptionThrowingRepository(Provider<Session> connectionProvider) {
            this.connectionProvider = connectionProvider;
        }

        @Transactional(rollbackOn = IOException.class, ignore = FileNotFoundException.class)
        public void addFoo(Foo foo) throws FileNotFoundException {
            add(connectionProvider.get(), foo);
            throw new FileNotFoundException();
        }
    }

    public static class IgnoredRuntimeExceptionThrowingRepository implements FooAdder<IllegalStateException> {
        private final Provider<Session> connectionProvider;

        @Inject
        public IgnoredRuntimeExceptionThrowingRepository(Provider<Session> connectionProvider) {
            this.connectionProvider = connectionProvider;
        }

        @Transactional(ignore = IllegalStateException.class)
        public void addFoo(Foo foo) {
            add(connectionProvider.get(), foo);
            throw new IllegalStateException();
        }
    }

    private static void add(Session session, Foo foo) {
        session.save(foo);
    }
}