package com.acuo.persist.services;

public interface Service<T> {

    <S extends T> S save(S entity);

    <S extends T> Iterable<S> save(Iterable<S> entities);

    void delete(Long id);

    void delete(T entity);

    void delete(Iterable<? extends T> entities);

    void deleteAll();

    <S extends T> S save(S entity, int depth);

    <S extends T> Iterable<S> save(Iterable<S> entities, int depth);

    <S extends T> S createOrUpdate(S entity);

    Iterable<T> findAll();

    Iterable<T> findAll(int depth);

    T find(Long id);

    T find(Long id, int depth);

    T findById(String id);

    T findById(String id, int depth);

}