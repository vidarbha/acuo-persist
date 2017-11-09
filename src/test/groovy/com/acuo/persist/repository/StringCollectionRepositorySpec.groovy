package com.acuo.persist.repository

import spock.lang.Specification
import spock.lang.Subject

import java.util.function.Predicate

class StringCollectionRepositorySpec extends Specification {

    @Subject
    FilteredRepository<String, Predicate<String>> repository

    void setup() {
        repository = new CollectionRepository<String>(new LinkedHashSet<String>())

        repository.add("a")
        repository.add("b")
        repository.add("c")
    }

    void "entities are added correctly"() {

        given:
        repository.add("d")

        when:
        def entities = repository.getCollection({ it -> true } as Predicate )

        then:
        entities.size() == 4
        entities.contains("d")
    }

    void "modifying the collection returned by getAll does not modify the repository's internal collection"() {
        given:
        def entities = repository.getAll()

        when:
        entities.clear()

        then:
        entities.size() == 0
        repository.getAll().size() == 3
    }

    void "getCollection method filters the entities correctly"() {
        given:
        def entities = repository.getCollection({ it -> it.equals("b") } as Predicate)

        expect:
        entities.size() == 1
        entities.contains("b")
    }

    void "modifying the Collection returned by getCollection does not modify the repository's internal collection"() {
        given:
        def entities = repository.getCollection( { it -> it.equals("b") } as Predicate)

        when:
        entities.remove("b")

        then:
        entities.size() == 0
        repository.getCollection({ it -> true } as Predicate).size() == 3
    }

    void "getEntity returns {@code null} when the repository is empty"() {
        given:
        for (final String ent : repository.getAll()) {
            repository.remove(ent)
        }

        when:
        def entity = repository.getEntity({ it -> it.equals("b") } as Predicate)

        then:
        entity == null
    }

    void "getEntity method filters the entities correctly"() {
        given:
        def entity = repository.getEntity({ it -> it.equals("b") } as Predicate)

        expect:
        entity == "b"
    }

    void "entities are removed correctly"() {
        given:
        repository.remove("b")

        when:
        def entities = repository.getCollection({ it -> true } as Predicate)

        then:
        entities.size() == 2
        !entities.contains("b")
    }

    void "entities are updated correctly"() {
       given:
       repository.update("c")

       when:
       def entities = repository.getCollection({ it -> true } as Predicate)

       then:
       entities.size() == 3
       entities.contains("c")
    }

    void "updating a non existing entity does not add it"() {
        given:
        repository.update("d")

        when:
        def entities = repository.getCollection({ it -> true } as Predicate)

        then:
        entities.size() == 3
        !entities.contains("d")
    }
}
