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

    private final ThreadLocal<Session> sessions = new ThreadLocal<>();

    private final Provider<SessionFactory> sessionFactoryProvider;

    private volatile SessionFactory sessionFactory;

    @Inject
    Neo4jPersistService(Provider<SessionFactory> sessionFactoryProvider) {
        this.sessionFactoryProvider = sessionFactoryProvider;
    }

    @Override
    public Session get() {
        if (!isWorking()) {
            begin();
        }

        Session session = sessions.get();

        if (session == null) {
            throw new IllegalStateException("Requested Session outside work unit. "
                    + "Try calling UnitOfWork.begin() first, or use a PersistFilter if you "
                    + "are inside a servlet environment.");
        }

        return session;
    }

    public boolean isWorking() {
        return sessions.get() != null;
    }

    @Override
    public synchronized void start() {
        if (sessionFactory != null)
        {
            throw new IllegalStateException("Persistence service was already initialized.");
        }

        this.sessionFactory = sessionFactoryProvider.get();
    }

    @Override
    public synchronized void stop() {
        // Do nothing...
    }

    @Override
    public void begin() {
        if(sessionFactory == null)
            start();
        if (sessions.get() != null) {
            throw new IllegalStateException(
                    "Work already begun on this thread. Looks like you have called UnitOfWork.begin() twice"
                            + " without a balancing call to end() in between.");
        }

        sessions.set(sessionFactory.openSession());
    }

    @Override
    public void end() {
        Session session = sessions.get();

        // Let's not penalize users for calling end() multiple times.
        if (session == null) {
            return;
        }

        sessions.remove();
    }

    @Override
    protected void startUp() {
        start();
    }

    @Override
    protected void shutDown() {
        stop();
    }
}
