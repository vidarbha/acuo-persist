package com.acuo.persist.core;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;
import org.neo4j.ogm.authentication.UsernamePasswordCredentials;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

@Singleton
public class Neo4jPersistService extends AbstractIdleService implements Provider<Session>, UnitOfWork, PersistService {

    private static final Logger LOG = LoggerFactory.getLogger(Neo4jPersistService.class);

    private final ThreadLocal<Session> sessions = new ThreadLocal<>();

    private final String[] persistencePackages;
    private final Configuration configuration;

    private volatile SessionFactory sessionFactory;

    @Inject
    Neo4jPersistService(@Named(PropertiesHelper.NEO4J_OGM_DRIVER) String driver,
                        @Named(PropertiesHelper.NEO4J_OGM_URL) String url,
                        @Named(PropertiesHelper.NEO4J_OGM_USERNAME) String userName,
                        @Named(PropertiesHelper.NEO4J_OGM_PASSWORD) String password,
                        Neo4jPersistModule.Packages packages) {
        LOG.info("Creating a Neo4j persistence service using driver [{}] and url[{}]", driver, url);
        this.persistencePackages = packages.value();
        this.configuration = new Configuration();
        this.configuration.driverConfiguration().setDriverClassName(driver).setURI(url)
                .setCredentials(new UsernamePasswordCredentials(userName, password));
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

        this.sessionFactory = new SessionFactory(configuration, persistencePackages);
    }

    @Override
    public synchronized void stop() {
        // Do nothing...
    }

    @Override
    public void begin() {
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
