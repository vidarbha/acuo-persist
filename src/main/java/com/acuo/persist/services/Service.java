package com.acuo.persist.services;

public interface Service<T> {

    Iterable<T> findAll();

    T find(Long id);

    void delete(Long id);

    T createOrUpdate(T object);

    T findById(String id);

    T createOrUpdateById(T entity, String id);

}