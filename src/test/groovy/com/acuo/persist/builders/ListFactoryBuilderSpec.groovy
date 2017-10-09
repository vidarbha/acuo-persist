package com.acuo.persist.builders

import com.acuo.persist.entity.Agreement
import com.acuo.persist.entity.MarginStatement
import com.acuo.persist.entity.VariationMargin
import com.acuo.persist.entity.enums.StatementStatus
import com.acuo.persist.modules.BuildersFactoryModule
import com.acuo.persist.modules.RepositoryModule
import com.netflix.governator.guice.test.InjectorCreationMode
import com.netflix.governator.guice.test.ModulesForTesting
import com.netflix.governator.guice.test.ReplaceWithMock
import org.neo4j.ogm.session.Session
import spock.lang.Specification

import javax.inject.Inject

@ModulesForTesting(value=[RepositoryModule, BuildersFactoryModule],
        injectorCreation=InjectorCreationMode.BEFORE_TEST_CLASS)
class ListFactoryBuilderSpec extends Specification {

    @Inject
    @ReplaceWithMock
    Session session

    @Inject
    FactoryBuilder builder

    def "create an empty margin statement"() {
        when:
        def statements = builder.list {
            statement()
        }

        then:
        statements.size == 1

        and:
        1 * session.save({MarginStatement statement -> statement.statementId == null })
    }

    def "building a new margin statement with an id"() {
        when:
        def statements = builder.list {
            statement(statementId: "ms1") {
                agreement(agreementId: "a1")
            }
        }

        then:
        statements.size == 1

        and:
        1 * session.save(_ as Agreement) >> { arguments ->
            Agreement agreement = arguments[0] as Agreement
            assert agreement.agreementId == "a1"
        }

        and:
        1 * session.save(_ as MarginStatement) >> { arguments ->
            MarginStatement statement = arguments[0] as MarginStatement
            assert statement.statementId == "ms1"
            assert statement.agreement.agreementId == "a1"
        }
    }

    def "building a margin statement with an existing id"() {
        setup:
        session.load(MarginStatement, *_) >> new MarginStatement(statementId: "ms2")
        session.load(Agreement, *_) >> new Agreement(agreementId: "a2")

        when:
        def statements = builder.list {
            statement(statementId: "ms1") {
                agreement(agreementId: "a1")
            }
        }

        then:
        statements.size == 1

        and:
        1 * session.save(_ as Agreement) >> { arguments ->
            Agreement agreement = arguments[0] as Agreement
            assert agreement.agreementId == "a1"
        }

        and:
        1 * session.save(_ as MarginStatement) >> { arguments ->
            MarginStatement statement = arguments[0] as MarginStatement
            assert statement.statementId == "ms1"
            assert statement.agreement.agreementId == "a1"
        }
    }

    def "building a statement with an agreement and a variation margin call"() {
        when:
        def account = builder.account(accountId: "act1")
        def sentFrom = builder.entity(legalEntityId: "l2", custodianAccounts: [account])
        def statements = builder.list {
            statement(statementId: "ms1", sentFrom: sentFrom) {
                agreement(agreementId: "a1")
                variation(itemId: "c1") {
                    step(status: StatementStatus.Expected)
                }
            }
        }

        then:
        statements.size == 1

        and:
        1 * session.save(_ as Agreement) >> { arguments ->
            Agreement agreement = arguments[0] as Agreement
            assert agreement.agreementId == "a1"
        }

        and:
        1 * session.save(_ as VariationMargin) >> { arguments ->
            VariationMargin call = arguments[0] as VariationMargin
            assert call.itemId == "c1"
        }

        and:
        1 * session.save(_ as MarginStatement) >> { arguments ->
            MarginStatement statement = arguments[0] as MarginStatement
            assert statement.statementId == "ms1"
            assert statement.agreement.agreementId == "a1"
            assert statement.sentFrom.legalEntityId == "l2"
            assert statement.sentFrom.custodianAccounts[0].accountId == "act1"
            assert statement.statementItems[0].itemId == "c1"
            assert statement.statementItems[0].firstStep != null
            assert statement.statementItems[0].lastStep != null
        }
    }

    def "building a client with one legal entity"() {
        when:
        def client = builder.client(firmId: "999"){
            entity(legalEntityId: "l1")
        }

        then:
        client != null
        client.firmId == "999"
        with(client.legalEntities) {
            size() == 1
            it*.legalEntityId.every {
                it in ["l1"]
            }
        }
    }

    def "building a client signs relationship"() {
        when:
        def clientSigns = builder.clientSigns(rounding: 10, MTA: 10, threshold:10){
            entity(legalEntityId: "l1")
            agreement(agreementId: "a1")
        }

        then:
        clientSigns != null
        clientSigns.legalEntity.legalEntityId == "l1"
        clientSigns.agreement.agreementId == "a1"

    }
}
