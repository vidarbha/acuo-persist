package com.acuo.persist.services

import com.acuo.common.model.ids.ClientId
import com.acuo.persist.entity.AssetTransfer
import com.google.inject.AbstractModule
import com.google.inject.Provides
import org.neo4j.ogm.session.Session
import spock.guice.UseModules
import spock.lang.Specification
import spock.lang.Subject

class Mod extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    Session session() {
        Session session = Mock()
        return session
    }
}

@UseModules(Mod)
class AssetTransferServiceImplSpec extends Specification {

    @InjectedMock

    MarginCallService marginCallService = Stub()
    AssetService assetService = Stub()
    CustodianAccountService custodianAccountService = Stub()
    AssetValuationService assetValuationService = Stub()
    FXRateService fxRateService = Stub()
    CollateralService collateralService = Stub()

    @Subject
    AssetTransferServiceImpl assetTransferService = new AssetTransferServiceImpl(marginCallService,
            assetService,
            custodianAccountService,
            assetValuationService,
            fxRateService,
            collateralService)

    def "find arriving asset transfer by clientId"() {
        when:
        def id = assetTransferService.findArrivingAssetTransferByClientId(ClientId.fromString("999"))

        then:
        !id
    }

    def "find departed asset transfer by clientId"() {
    }

    def "send asset"() {
    }

    def "receive asset"() {
    }

    def "get pledged assets"() {
    }
}
