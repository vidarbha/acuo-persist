package com.acuo.persist.services;

import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Provider;
import java.io.Serializable;

@Transactional
public abstract class AbstractService<T, ID extends Serializable> implements Service<T, ID> {

    protected final GenericService<T, ID> dao;

    public AbstractService(Provider<Session> session) {
        this.dao = new GenericService<>(session, getEntityType());
    }

    public abstract Class<T> getEntityType();

    @Override
    public <S extends T> S save(S entity) {
        return dao.save(entity);
    }

    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        return dao.save(entities);
    }

    @Override
    public void delete(ID id) {
        dao.delete(id);
    }

    @Override
    public void delete(T entity) {
        dao.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        dao.delete(entities);
    }

    @Override
    public void deleteAll() {
        dao.deleteAll();
    }

    @Override
    public <S extends T> S save(S entity, int depth) {
        return dao.save(entity, depth);
    }

    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities, int depth) {
        return dao.save(entities, depth);
    }

    @Override
    public <S extends T> S createOrUpdate(S entity) {
        return dao.createOrUpdate(entity);
    }

    @Override
    public Iterable<T> findAll() {
        return dao.findAll();
    }

    @Override
    public Iterable<T> findAll(int depth) {
        return dao.findAll(depth);
    }

    @Override
    public T find(ID id) {
        return dao.find(id);
    }

    @Override
    public T find(ID id, int depth) {
        return dao.find(id, depth);
    }


}
