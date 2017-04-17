package com.acuo.persist.services;

import com.acuo.common.util.ArgChecker;
import com.acuo.persist.entity.Entity;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

@Slf4j
@Transactional
public abstract class GenericService<T> implements Service<T> {

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
        sessionProvider.get().save(entity);
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        ArgChecker.notNull(entities, "entities");
        ArgChecker.notEmpty(entities, "entities");
        if (log.isDebugEnabled()) {
            log.debug("save {}",entities);
        }
        sessionProvider.get().save(entities);
        return entities;
    }

    @Override
    public void delete(Long id) {
        ArgChecker.notNull(id, "id");
        if (log.isDebugEnabled()) {
            log.debug("delete {}", id);
        }
        sessionProvider.get().delete(sessionProvider.get().load(getEntityType(), id));
    }

    @Override
    public void delete(T entity) {
        ArgChecker.notNull(entity, "entity");
        if (log.isDebugEnabled()) {
            log.debug("delete {}", entity);
        }
        sessionProvider.get().delete(entity);
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        ArgChecker.notNull(entities, "entities");
        ArgChecker.notEmpty(entities, "entities");
        if (log.isDebugEnabled()) {
            log.debug("delete {}", entities);
        }
        sessionProvider.get().delete(entities);
    }

    @Override
    public void deleteAll() {
        sessionProvider.get().deleteAll(getEntityType());
    }

    @Override
    public <S extends T> S save(S entity, int depth) {
        ArgChecker.notNull(entity, "entity");
        if (log.isDebugEnabled()) {
            log.debug("save {} depth {}", entity, depth);
        }
        sessionProvider.get().save(entity, depth);
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities, int depth) {
        ArgChecker.notNull(entities, "entities");
        ArgChecker.notEmpty(entities, "entities");
        if (log.isDebugEnabled()) {
            log.debug("save {} depth {}", entities, depth);
        }
        sessionProvider.get().save(entities, depth);
        return entities;
    }

    @Override
    public <S extends T> S createOrUpdate(S entity) {
        ArgChecker.notNull(entity, "entity");
        if (log.isDebugEnabled()) {
            log.debug("createOrUpdate {}", entity);
        }
        sessionProvider.get().save(entity);
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
        return sessionProvider.get().loadAll(getEntityType(), depth);
    }

    @Override
    public T find(Long id) {
        ArgChecker.notNull(id, "id");
        if (log.isDebugEnabled()) {
            log.debug("find {}", id);
        }
        return sessionProvider.get().load(getEntityType(), id, DEPTH_ENTITY);
    }

    @Override
    public T find(Long id, int depth) {
        ArgChecker.notNull(id, "id");
        if (log.isDebugEnabled()) {
            log.debug("find {} depth {}", id, depth);
        }
        return sessionProvider.get().load(getEntityType(), id, depth);
    }

    @Override
    public T findById(String id) {
        ArgChecker.notNull(id, "id");
        if (log.isDebugEnabled()) {
            log.debug("findById {}", id);
        }
        String query = "MATCH (i:" + getEntityType().getSimpleName() + " {id: {id} }) return i";
        T entity = sessionProvider.get().queryForObject(getEntityType(), query, ImmutableMap.of("id",id));
        if(entity != null)
            return find(((Entity) entity).getId(), DEPTH_ENTITY);
        else
            return null;
    }

    @Override
    public T findById(String id, int depth) {
        ArgChecker.notNull(id, "id");
        if (log.isDebugEnabled()) {
            log.debug("findById {} depth {}", id, depth);
        }
        String query = "MATCH (i:" + getEntityType().getSimpleName() + " {id: {id} }) return i";
        T entity = sessionProvider.get().queryForObject(getEntityType(), query, ImmutableMap.of("id",id));
        if(entity != null)
            return find(((Entity) entity).getId(), depth);
        else
            return null;
    }
}