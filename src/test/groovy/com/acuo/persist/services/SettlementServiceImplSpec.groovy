package com.acuo.persist.services

import com.acuo.common.ids.AssetId
import com.acuo.common.model.results.AssetSettlementDate
import com.acuo.persist.entity.Settlement
import com.acuo.persist.modules.BuildersFactoryModule
import com.acuo.persist.modules.ConfigurationTestModule
import com.acuo.persist.modules.ImportTestServiceModule
import com.acuo.persist.modules.InProcessNeo4jServerModule
import com.acuo.persist.utils.EntityStoreFixture
import spock.guice.UseModules
import spock.lang.Specification

import javax.inject.Inject
import java.time.LocalDate

@UseModules([
        ConfigurationTestModule,
        InProcessNeo4jServerModule,
        ImportTestServiceModule,
        BuildersFactoryModule
])
class SettlementServiceImplSpec extends Specification {

    @Delegate
    @Inject
    EntityStoreFixture entityStore

    @Inject
    SettlementService service

    void setup() throws Exception {
        importService.deleteAll()
    }

    void "Creating two times a settlement node for the same asset returns the same node"() {
        given:
        AssetId assetId = AssetId.fromString("DUMMY")
        builder.asset(assetId: assetId)

        when:
        Settlement settlement = service.getSettlementFor(assetId)

        then:
        settlement == null

        when:
        settlement = service.getOrCreateSettlementFor(assetId)

        then:
        settlement != null

        when:
        def newOne = service.getOrCreateSettlementFor(assetId)

        then:
        settlement == newOne

    }

    void "Create new settlement node for an asset and then consequently get it"() {
        given:
        AssetId assetId = AssetId.fromString("DUMMY")
        builder.asset(assetId: assetId)

        when:
        Settlement settlement = service.getSettlementFor(assetId)

        then:
        settlement == null

        when:
        settlement = service.getOrCreateSettlementFor(assetId)

        then:
        settlement != null

        when:
        settlement = service.getSettlementFor(assetId)

        then:
        settlement != null

    }

    void "Persist a new settlement date for a given asset"() {
        given:
        AssetId assetId = AssetId.fromString("DUMMY")
        builder.asset(assetId: assetId)

        and:
        AssetSettlementDate settlementDate = new AssetSettlementDate(assetId: assetId.toString(),
                settlementDate: LocalDate.now())

        when:
        Settlement settlement = service.persist(settlementDate)

        then:
        settlement != null

        when:
        settlement = service.getSettlementFor(assetId)

        then:
        settlement != null
        settlement.latestSettlementDate != null
        settlement.settlementDates.size() == 1
    }
}
