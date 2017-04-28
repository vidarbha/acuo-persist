package com.acuo.persist.services;

import java.io.Serializable;

public interface Service<T, ID extends Serializable> {

    <S extends T> S save(S entity);

    <S extends T> Iterable<S> save(Iterable<S> entities);

    void delete(ID id);

    void delete(T entity);

    void delete(Iterable<? extends T> entities);

    void deleteAll();

    <S extends T> S save(S entity, int depth);

    <S extends T> Iterable<S> save(Iterable<S> entities, int depth);

    <S extends T> S createOrUpdate(S entity);

    Iterable<T> findAll();

    Iterable<T> findAll(int depth);

    T find(ID id);

    T find(ID id, int depth);
}