package com.acuo.persist.utils

import com.acuo.common.ids.ClientId
import com.acuo.persist.modules.BuildersFactoryModule
import com.acuo.persist.modules.ConfigurationTestModule
import com.acuo.persist.modules.ImportServiceModule
import com.acuo.persist.modules.InProcessNeo4jServerModule
import com.acuo.persist.services.AssetService
import com.acuo.persist.services.ClientService
import com.acuo.persist.services.ClientSignsRelationService
import com.acuo.persist.services.MarginCallService
import spock.guice.UseModules
import spock.lang.Specification
import spock.lang.Subject

import javax.inject.Inject

@UseModules([
        ConfigurationTestModule,
        InProcessNeo4jServerModule,
        ImportServiceModule,
        BuildersFactoryModule
])
class EntityStoreFixtureSpec extends Specification {

    @Subject
    @Inject
    EntityStoreFixture fixture

    @Inject
    ClientService clientService

    @Inject
    ClientSignsRelationService clientSignsRelationService

    @Inject
    MarginCallService marginCallService

    @Inject
    AssetService assetService

    void setup() {
        fixture.install()
    }

    def "retrieve the client nodes"() {
        expect:
        def clients = clientService.findAll()
        clients != null
        with(clients) {
            size == 2
            it*.every {
                it in ["999","888"]
            }
        }
    }

    def "retrieve the client signs relationship"() {
        expect:
        def nodes = clientSignsRelationService.findAll(2)
        nodes != null
        with(nodes) {
            size == 2
            it*.agreement.agreementId.every {
                it in ["bilateral-agreement", "cleared-agreement"]
            }
        }
    }

    def "retrieve the margin calls nodes"() {
        expect:
        def nodes = marginCallService.findAll(2)
        nodes != null
        with(nodes){
            size == 6
        }
    }

    def "retrieve all assets of a client"() {
        expect:
        def assets = assetService.findAssets(ClientId.fromString("999"))
        assets != null
        with(assets) {
            size == 2
            assets[0].model() != null
        }
    }
}
