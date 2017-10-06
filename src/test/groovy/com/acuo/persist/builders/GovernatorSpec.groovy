package com.acuo.persist.builders

import com.netflix.governator.guice.test.InjectorCreationMode
import com.netflix.governator.guice.test.ModulesForTesting
import spock.lang.Specification

@ModulesForTesting(injectorCreation = InjectorCreationMode.BEFORE_EACH_TEST_METHOD)
class GovernatorSpec extends Specification {

    def "dummy test"() {
        expect:
        1 + 1 == 2
    }
}
