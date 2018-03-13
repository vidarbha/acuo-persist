package com.acuo.persist.services

import com.acuo.common.model.ids.AssetId
import com.acuo.common.model.ids.ClientId
import com.acuo.persist.entity.Asset
import org.neo4j.ogm.model.Result
import org.neo4j.ogm.session.Session
import spock.lang.Specification
import spock.lang.Subject

class AssetServiceImplSpec extends Specification {

    Session session = Mock()

    @Subject AssetService assetService = new AssetServiceImpl({-> session})

    def "find available asset by client id"() {
        given: "a client id"
        def clientId = ClientId.fromString("999")

        when: "we search for all asset transfer"
        assetService.findAvailableAssetByClientId(clientId)

        then: "we query the db once with the right entity type"
        1 * session.query(Asset, AssetServiceImpl.AVAILABLE_ASSET, {it.any { key, value -> key == 'clientId' && value == '999'} })
    }

    def "find available asset by client id and call id"() {
        given:
        def clientId = ClientId.fromString("999")
        def callId = "mcp1"

        when:
        assetService.findAvailableAssetByClientIdAndCallId(clientId, callId)

        then:
        1 * session.query(Asset, AssetServiceImpl.ELIGIBLE_ASSET_BY_CLIENT_AND_CALLID, {it.any { key, value -> key == 'clientId' && value == '999'} })
    }

    def "total haircut"() {
        given:
        def assetId = AssetId.fromString("USD")
        def callId = "mcp1"
        def result = Stub(Result)
        result.iterator() >> [['totalHaircut':10d]].iterator()

        when:
        def haircut = assetService.totalHaircut(assetId, callId)

        then:
        1 * session.query(AssetServiceImpl.TOTAL_HAIRCUT, {it.any { key, value -> key == 'assetId' && value == 'USD'} }) >> result
        haircut == 10d
    }
}
