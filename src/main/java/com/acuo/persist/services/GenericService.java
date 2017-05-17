package com.acuo.persist.services;

import com.acuo.common.util.ArgChecker;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.Serializable;

@Slf4j
@Transactional
public abstract class GenericService<T, ID extends Serializable> implements Service<T, ID> {

    private static final int DEPTH_LIST = 0;
    private static final int DEPTH_ENTITY = 1;

    @Inject
    protected Provider<Session> sessionProvider;

    public abstract Class<T> getEntityType();

    @Override
    public <S extends T> S save(S entity) {
        ArgChecker.notNull(entity, "entity");
        if (log.isDebugEnabled()) {
            log.debug("save {}",entity);
        }
        getSession().save(entity);
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        ArgChecker.notNull(entities, "entities");
        ArgChecker.notEmpty(entities, "entities");
        if (log.isDebugEnabled()) {
            log.debug("save {}",entities);
        }
        getSession().save(entities);
        return entities;
    }

    @Override
    public void delete(ID id) {
        ArgChecker.notNull(id, "id");
        if (log.isDebugEnabled()) {
            log.debug("delete {}", id);
        }
        getSession().delete(getSession().load(getEntityType(), id));
    }

    @Override
    public void delete(T entity) {
        ArgChecker.notNull(entity, "entity");
        if (log.isDebugEnabled()) {
            log.debug("delete {}", entity);
        }
        getSession().delete(entity);
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        ArgChecker.notNull(entities, "entities");
        ArgChecker.notEmpty(entities, "entities");
        if (log.isDebugEnabled()) {
            log.debug("delete {}", entities);
        }
        getSession().delete(entities);
    }

    @Override
    public void deleteAll() {
        getSession().deleteAll(getEntityType());
    }

    @Override
    public <S extends T> S save(S entity, int depth) {
        ArgChecker.notNull(entity, "entity");
        if (log.isDebugEnabled()) {
            log.debug("save {} depth {}", entity, depth);
        }
        getSession().save(entity, depth);
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities, int depth) {
        ArgChecker.notNull(entities, "entities");
        ArgChecker.notEmpty(entities, "entities");
        if (log.isDebugEnabled()) {
            log.debug("save {} depth {}", entities, depth);
        }
        getSession().save(entities, depth);
        return entities;
    }

    @Override
    public <S extends T> S createOrUpdate(S entity) {
        ArgChecker.notNull(entity, "entity");
        if (log.isDebugEnabled()) {
            log.debug("createOrUpdate {}", entity);
        }
        getSession().save(entity);
        return entity;
    }

    @Override
    public Iterable<T> findAll() {
        return findAll(DEPTH_LIST);
    }

    @Override
    public Iterable<T> findAll(int depth) {
        if (log.isDebugEnabled()) {
            log.debug("findAll depth [{}]", depth);
        }
        return getSession().loadAll(getEntityType(), depth);
    }

    @Override
    public T find(ID id) {
        ArgChecker.notNull(id, "id");
        if (log.isDebugEnabled()) {
            log.debug("find {}", id);
        }
        return getSession().load(getEntityType(), id, DEPTH_ENTITY);
    }

    @Override
    public T find(ID id, int depth) {
        ArgChecker.notNull(id, "id");
        if (log.isDebugEnabled()) {
            log.debug("find {} depth {}", id, depth);
        }
        return getSession().load(getEntityType(), id, depth);
    }

    private Session getSession() {
        final Session session = sessionProvider.get();
        if (log.isDebugEnabled()) {
            log.debug("session {}", session.hashCode());
        }
        return session;
    }
}