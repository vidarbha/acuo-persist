package com.acuo.persist.services;

import com.acuo.common.util.ArgChecker;
import com.acuo.persist.entity.Entity;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;

public abstract class GenericService<T> implements Service<T> {

    private static final int DEPTH_LIST = 0;
    private static final int DEPTH_ENTITY = 1;

    @Inject
    protected Session session;

    public abstract Class<T> getEntityType();

    @Transactional
    @Override
    public <S extends T> S save(S entity) {
        ArgChecker.notNull(entity, "entity");
        session.save(entity);
        return entity;
    }

    @Transactional
    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        ArgChecker.notNull(entities, "entities");
        ArgChecker.notEmpty(entities, "entitties");
        for (S entity : entities) {
            session.save(entity);
        }
        return entities;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        ArgChecker.notNull(id, "id");
        session.delete(session.load(getEntityType(), id));
    }

    @Transactional
    @Override
    public void delete(T entity) {
        ArgChecker.notNull(entity, "entity");
        session.delete(entity);
    }

    @Transactional
    @Override
    public void delete(Iterable<? extends T> entities) {
        ArgChecker.notNull(entities, "entities");
        ArgChecker.notEmpty(entities, "entitties");
        for (T entity : entities) {
            session.delete(entity);
        }
    }

    @Transactional
    @Override
    public void deleteAll() {
        session.deleteAll(getEntityType());
    }

    @Transactional
    @Override
    public <S extends T> S save(S entity, int depth) {
        ArgChecker.notNull(entity, "entity");
        session.save(entity, depth);
        return entity;
    }

    @Transactional
    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities, int depth) {
        ArgChecker.notNull(entities, "entities");
        ArgChecker.notEmpty(entities, "entitties");
        session.save(entities, depth);
        return entities;
    }

    @Transactional
    @Override
    public <S extends T> S createOrUpdate(S entity) {
        ArgChecker.notNull(entity, "entity");
        session.save(entity);
        return entity;
    }

    @Override
    public Iterable<T> findAll() {
        return session.loadAll(getEntityType(), DEPTH_LIST);
    }

    @Override
    public T find(Long id) {
        ArgChecker.notNull(id, "id");
        return session.load(getEntityType(), id, DEPTH_ENTITY);
    }

    @Override
    public T find(Long id, int depth) {
        ArgChecker.notNull(id, "id");
        return session.load(getEntityType(), id, depth);
    }

    @Override
    public T findById(String id) {
        ArgChecker.notNull(id, "id");
        String query = "MATCH (i:" + getEntityType().getSimpleName() + " {id: {id} }) return i";
        T entity = session.queryForObject(getEntityType(), query, ImmutableMap.of("id",id));
        if(entity != null)
            return find(((Entity) entity).getId(), DEPTH_ENTITY);
        else
            return null;
    }

    @Override
    public T findById(String id, int depth) {
        ArgChecker.notNull(id, "id");
        String query = "MATCH (i:" + getEntityType().getSimpleName() + " {id: {id} }) return i";
        T entity = session.queryForObject(getEntityType(), query, ImmutableMap.of("id",id));
        if(entity != null)
            return find(((Entity) entity).getId(), depth);
        else
            return null;
    }
}