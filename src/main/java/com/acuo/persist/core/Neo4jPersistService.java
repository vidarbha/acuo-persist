package com.acuo.persist.core;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import javax.inject.Inject;
import javax.inject.Provider;

@Singleton
@Slf4j
public class Neo4jPersistService extends AbstractIdleService implements Provider<Session>, UnitOfWork, PersistService {

    private final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<Integer> beginCountThreadLocal = new ThreadLocal<Integer>();

    private final SessionFactory sessionFactory;

    @Inject
    Neo4jPersistService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    protected void startUp() {
        start();
    }

    @Override
    protected void shutDown() {
        stop();
    }

    @Override
    public void start() {
    /*
     * Nothing to do here really. We're injecting the DataSource this uses directly, not creating it
     * when the service starts, and there isn't any concept of "closing" the DataSource itself.
     */
    }

    @Override
    public void stop() {
    /*
     * Nothing to do here either. Some DataSources have a close() method, but it's not part of the
     * interface so....
     */
    }

    @Override
    public void begin() {
        if (!isWorking()) {
            sessionThreadLocal.set(getSession());
            beginCountThreadLocal.set(1);
        } else {
            beginCountThreadLocal.set(beginCountThreadLocal.get() + 1);
        }
    }

    @Override
    public void end() {
        Integer beginCount = beginCountThreadLocal.get();
        if (beginCount == null)
            return;

        beginCountThreadLocal.set(--beginCount);

        if (beginCount == 0) {
            closeSession(sessionThreadLocal.get());
            sessionThreadLocal.remove();
            beginCountThreadLocal.remove();
        }
    }

    /**
     * @return {@code true} if work is currently active on the current thread; {@code false} otherwise.
     */
    public boolean isWorking() {
        return sessionThreadLocal.get() != null;
    }

    @Override
    public Session get() {
        Session result = sessionThreadLocal.get();
        log.debug("session {}, count {}", result, beginCountThreadLocal.get());
        if (result == null) {
            throw new IllegalStateException("No UnitOfWork active while attempting to retrieve Connection. " +
                    "Be sure to call UnitOfWork.begin() before retrieving a Connection or that you retrieve " +
                    "the Connection in a @Transactional method.");
        }
        return result;
    }

    private Session getSession() {
        return sessionFactory.openSession();
    }

    private void closeSession(Session conn) {

    }
}
