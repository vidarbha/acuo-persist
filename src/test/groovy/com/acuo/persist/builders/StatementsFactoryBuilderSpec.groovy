package com.acuo.persist.builders

import com.acuo.persist.modules.BuildersFactoryModule
import com.acuo.persist.modules.RepositoryModule
import com.netflix.governator.guice.test.ModulesForTesting
import com.netflix.governator.guice.test.ReplaceWithMock
import org.neo4j.ogm.session.Session
import spock.lang.Specification

import javax.inject.Inject

@ModulesForTesting([RepositoryModule, BuildersFactoryModule])
class StatementsFactoryBuilderSpec extends Specification {

    @Inject
    @ReplaceWithMock
    Session session

    @Inject
    StatementsFactoryBuilder builder

    def "create an empty margin statement"() {
        when:
        def statements = builder.statements {
            statement() {
                agreement()
            }
        }

        then:
        statements.size == 1

        //and:
        //1 * session.save(statements[0])
    }

    def "create a margin statement with an id"() {
        expect:
        def statements = builder.statements {
            statement(statementId: "ms1")
        }
        statements.size == 1
        statements.any { it.getStatementId() == "ms1" }
    }
}
