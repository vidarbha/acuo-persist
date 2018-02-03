package com.acuo.persist.services

import com.acuo.common.ids.AssetId
import com.acuo.common.ids.ClientId
import com.acuo.persist.modules.BuildersFactoryModule
import com.acuo.persist.modules.ConfigurationTestModule
import com.acuo.persist.modules.ImportTestServiceModule
import com.acuo.persist.modules.InProcessNeo4jServerModule
import com.acuo.persist.utils.EntityStoreFixture
import org.neo4j.ogm.model.Result
import spock.guice.UseModules
import spock.lang.Specification
import spock.lang.Subject

import javax.inject.Inject

@UseModules([
        ConfigurationTestModule,
        InProcessNeo4jServerModule,
        ImportTestServiceModule,
        BuildersFactoryModule
])
class AssetServiceImplSpec extends Specification {

    @Delegate
    @Inject
    EntityStoreFixture entityStore

    @Subject
    @Inject
    AssetService assetService

    def clientId = ClientId.fromString("999")

    void setup(){
        entityStore.install()
    }

    def "find available asset by client id"() {
        when:
        def assets = assetService.findAvailableAssetByClientId(clientId)

        then:
        assets != null
        with(assets) {
            size == 2
        }

    }

    def "find available asset by client id and call id"() {
        given:
        def callId = "c1"

        when:
        def assets = assetService.findAvailableAssetByClientIdAndCallId(clientId, callId)

        then:
        assets != null
        with(assets) {
            size == 1
        }
    }

    def "total haircut"() {
        given:
        def assetId = AssetId.fromString("USD")
        def callId = "b1"
        def result = Stub(Result)
        result.iterator() >> [['totalHaircut':10d]].iterator()

        when:
        def haircut = assetService.totalHaircut(assetId, callId)

        then:
        haircut == 0.0d
    }
}
