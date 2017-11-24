package com.acuo.persist.services

import com.acuo.common.ids.AssetId
import com.acuo.common.ids.ClientId
import com.acuo.persist.entity.*
import com.acuo.persist.entity.enums.AssetTransferStatus
import org.neo4j.ogm.session.Session
import spock.lang.Specification
import spock.lang.Subject

class AssetTransferServiceImplSpec extends Specification {

    Session session = Mock()
    MarginCallService marginCallService = Stub()
    AssetService assetService = Stub()
    CustodianAccountService custodianAccountService = Mock()
    AssetValuationService assetValuationService = Stub()
    FXRateService fxRateService = Stub()
    CollateralService collateralService = Stub()

    @Subject
    AssetTransferServiceImpl assetTransferService = new AssetTransferServiceImpl(
            {-> session},
            marginCallService,
            assetService,
            custodianAccountService,
            assetValuationService,
            fxRateService,
            collateralService)

    def "find arriving asset transfer by clientId"() {
        given: "a client id"
        def clientId = ClientId.fromString("999")

        when: "we search for all asset transfer"
        assetTransferService.findAssetTransferByClientIdAndStatus(clientId, AssetTransferStatus.Arriving)

        then: "we query the db once with the right entity type"
        1 * session.query(AssetTransfer, _ as String, {it.any {key, value -> key == 'clientId' && value == '999'} })
    }

    def "find departed asset transfer by clientId"() {
        given: "a client id"
        def clientId = ClientId.fromString("999")

        when: "we search for all asset transfer"
        assetTransferService.findAssetTransferByClientIdAndStatus(clientId, AssetTransferStatus.Departed)

        then: "we query the db once with the right entity type"
        1 * session.query(AssetTransfer, _ as String, {it.any {key, value -> key == 'clientId' && value == '999'} })
    }

    def "send asset"() {
        given:
        def marginCallId = "mcp1"
        def assetId = AssetId.fromString("USD")
        def call = Mock(MarginCall)
        def statement = Stub(MarginStatement)
        def entity = Stub(LegalEntity)
        def custodian = Stub(CustodianAccount)
        marginCallService.callById(marginCallId) >> call
        call.getMarginStatement() >> statement
        statement.getDirectedTo() >> entity
        entity.getCustodianAccounts() >> [custodian]

        when:
        assetTransferService.sendAsset(marginCallId, assetId, 10, "Acct1")

        then:
        1 * call.getCurrency()
        2 * call.getItemId()
        2 * session.save(*_)
    }

    def "receive asset"() {
        given:
        def marginCallId = "mcp1"
        def assetId = AssetId.fromString("USD")
        def call = GroovyMock(MarginCall)
        def statement = Stub(MarginStatement)
        def entity = Stub(LegalEntity)
        def custodian = Stub(CustodianAccount)
        marginCallService.callById(marginCallId) >> call
        call.getMarginStatement() >> statement
        statement.getDirectedTo() >> entity
        entity.getCustodianAccounts() >> [custodian]

        custodianAccountService.find("Acct1", 2) >> custodian
        custodianAccountService.counterPartyCustodianAccountsFor(_) >> [custodian]

        when:
        assetTransferService.receiveAsset(marginCallId, assetId, 10, "Acct1")

        then:
        1 * call.getCurrency()
    }

    def "get pledged assets"() {
        when: "we query for all pledged assets"
        assetTransferService.getPledgedAssets()

        then: "we query the db with no parameters"
        1 * session.query(_ as String, [:])
    }
}
