package com.acuo.persist.repository

import groovy.transform.Immutable
import spock.lang.Specification

import java.util.function.Predicate

class CustomUpdateCollectionRepositorySpec extends Specification {

    /**
     * The repository being tested.
     */
    FilteredRepository<TestClass, Predicate<TestClass>> repository

    /**
     * This is a test class for testing the {@code Repository} using a class
     * with an internal state.
     *
     * In this case, the state will be the stored value, while the name
     * identifies it.
     */
    @Immutable
    class TestClass {

        /**
         * Name of the class, which will identify it.
         */
        String name

    }

    /**
     * Restores the repository state before each test.
     */
    void setup() {
        repository = new CollectionRepository<TestClass>(new LinkedHashSet<TestClass>())

        repository.add(new TestClass("a"))
        repository.add(new TestClass("b"))
        repository.add(new TestClass("c"))
    }

    void "updating an existing entity does not add it"() {
        given:
        def entity = new TestClass("a")

        when:
        repository.update(entity)

        and:
        def entities = repository.getCollection({ it -> true } as Predicate)

        then:
        entities.size() == 3
        entities.contains(entity)
    }

    void "updating a non existing entity does not add it"() {
        given:
        def entity = new TestClass("d")

        when:
        repository.update(new TestClass("d"))

        and:
        def entities = repository.getCollection({ it -> true } as Predicate)

        then:
        entities.size() == 3
        !entities.contains(entity)
    }
}
