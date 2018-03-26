package com.acuo.persist.learning

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.core.util.StatusPrinter
import com.acuo.common.model.margin.Types
import com.acuo.persist.core.DataLoader
import com.acuo.persist.core.Neo4jDataImporter
import com.acuo.persist.core.SessionDataLoader
import com.acuo.persist.learning.dummy.*
import com.acuo.persist.services.AbstractService
import com.google.common.collect.ImmutableMap
import com.google.inject.persist.Transactional
import org.junit.Rule
import org.neo4j.harness.junit.EnterpriseNeo4jRule
import org.neo4j.harness.junit.Neo4jRule
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.Session
import org.neo4j.ogm.session.SessionFactory
import org.slf4j.LoggerFactory
import spock.lang.Specification

import static com.google.common.collect.ImmutableMap.of

class DummyAssetServiceSpec extends Specification {

    @Rule
    Neo4jRule neo4jRule = new EnterpriseNeo4jRule().withConfig("dbms.security.auth_enabled", "false")

    Session session
    DataLoader dataLoader
    Neo4jDataImporter dataImporter

    DummyAssetService assetService
    DummyAssetTransferService assetTransferService
    DummyAssetPledgeService assetPledgeService
    DummyHoldsService holdsService

    static class DummyHoldsService extends AbstractService<DummyHolds, String> {

        DummyHoldsService(Session session) {
            super({ -> session })
        }

        @Override
        Class<DummyHolds> getEntityType() {
            DummyHolds
        }
    }

    static class DummyAssetService extends AbstractService<DummyAsset, String> {

        private final DummyHoldsService holdsService

        DummyAssetService(Session session, DummyHoldsService holdsService) {
            super({ -> session })
            this.holdsService = holdsService
        }

        void removeQuantity(String assetId, Double quantity) {
            this.quantity(assetId, quantity, { a, b -> a - b })
        }

        void quantity(String assetId, Double quantity, Closure<Double> op) {
            DummyAsset asset = find(assetId, 1)
            DummyHolds holds = asset.getHolds()
            if (holds != null) {
                final Double result = op.call(holds.getAvailableQuantity(), quantity)
                holds.setAvailableQuantity(result)
                holdsService.save(holds, 1)
            }
        }

        void clear() {
            dao.session.clear()
        }

        @Override
        Class<DummyAsset> getEntityType() {
            DummyAsset
        }
    }

    static class DummyAssetTransferService extends AbstractService<DummyAssetTransfer, String> {

        private final DummyAssetService assetService

        DummyAssetTransferService(Session session, DummyAssetService assetService) {
            super({ -> session })
            this.assetService = assetService
        }

        @Override
        Class<DummyAssetTransfer> getEntityType() {
            DummyAssetTransfer
        }

        DummyAssetTransfer create(String assetId) {
            DummyAsset asset = assetService.find(assetId, 2)
            DummyAssetTransfer transfer = new DummyAssetTransfer(assertTransferId: assetId + "-Transfer", of: asset)
            save(transfer, 1)
        }

        void setCustodianAccount(DummyAssetTransfer assetTransfer, String fromAccount) {
            DummyCustodianAccount custodianAccount = new DummyCustodianAccount(accountId: fromAccount)
            assetTransfer.setFrom(custodianAccount)
            save(assetTransfer, 1)
        }


    }

    static class DummyAssetPledgeService extends AbstractService<DummyAssetPledge, Long> {

        DummyAssetService assetService

        DummyAssetPledgeService(Session session, DummyAssetService assetService) {
            super({ -> session })
            this.assetService = assetService
        }

        @Override
        Class<DummyAssetPledge> getEntityType() {
            DummyAssetPledge
        }

        @Transactional
        DummyAssetPledge getFor(String assetId, Types.MarginType marginType) {
            String query =
                    "MATCH p=(assetPledge:DummyAssetPledge {marginType: {marginType}})" +
                            "-[:OF]->(asset:DummyAsset {id:{id}}) " +
                            "RETURN p, nodes(p), relationships(p)"
            final ImmutableMap<String, String> parameters = of("id", assetId,
                    "marginType", marginType.name())
            return dao.getSession().queryForObject(DummyAssetPledge.class, query, parameters)
        }

        @Transactional
        DummyAssetPledge getOrCreateFor(String assetId, Types.MarginType marginType) {
            DummyAssetPledge assetPledge = getFor(assetId, marginType)
            if (assetPledge == null) {
                assetPledge = new DummyAssetPledge()
                assetPledge.setMarginType(marginType)
                assetPledge.setAsset(assetService.find(assetId))
                assetPledge = createOrUpdate(assetPledge)
            }
            return assetPledge
        }

        void clear() {
            dao.session.clear()
        }

    }

    void setup() {
        StatusPrinter.print((LoggerContext) LoggerFactory.getILoggerFactory())

        session = createSession()

        this.holdsService = new DummyHoldsService(session)
        this.assetService = new DummyAssetService(session, holdsService)
        this.assetTransferService = new DummyAssetTransferService(session, assetService)
        this.assetPledgeService = new DummyAssetPledgeService(session, assetService)
    }

    private void loadAssets(session) {
        dataLoader = new SessionDataLoader({ -> session })
        dataImporter = new Neo4jDataImporter(dataLoader,
                "graph-data",
                "develop",
                "graph-data",
                "dummyAssets",
                "%s/cypher/%s.load",
                "ACUO")

        dataImporter.reload()
    }

    void cleanup() {
        session.purgeDatabase()
    }

    void "create one simple asset"(){
        given:
        DummyAsset asset = new DummyAsset(assetId: "USD")

        when:
        assetService.save(asset)

        then:
        def all = assetService.findAll()
        all.size() == 1
    }

    void "create one asset with one rule"() {
        given:
        DummyAsset asset = new DummyAsset(assetId: "USD")
        DummyRule rule = new DummyRule(haircut: 10)

        when:
        asset.setRules([rule] as Set)
        assetService.save(asset)

        then:
        def usd = assetService.find("USD")
        usd != null
        with(usd.getRules()) {
            it != null
            it.size() == 1
            it.first().haircut == 10
        }
    }

    void "create one asset with several rules"() {
        expect:
        loadAssets(session)
        def usd = assetService.find("USD")
        usd != null
        with(usd.getRules()) {
            it != null
            it.size() == 100
        }
    }

    void "create an asset transfer of an asset with rule"() {
        given:
        loadAssets(session)

        when:
        def asset = assetService.find("USD")
        DummyAssetTransfer transfer = new DummyAssetTransfer(assertTransferId: "USD-Transfer", of: asset)

        then:
        asset.rules.size() == 100
        asset.holds.availableQuantity == 100

        when:
        assetTransferService.save(transfer)

        then:
        def result = assetTransferService.find("USD-Transfer")
        result != null
        result.of == asset
        result.of.rules.size() == 100
        result.of.holds.availableQuantity == 100
    }

    void "create an asset pledge from an asset transfer"() {
        given:
        loadAssets(session)

        when:
        def asset = assetService.find("USD")
        def transfer = assetTransferService.create(asset.assetId)

        then:
        asset.rules.size() == 100
        asset.holds.availableQuantity == 100

        when:
        def assetPledge = assetPledgeService.getOrCreateFor("USD", Types.MarginType.Initial)
        assetPledgeService.clear()
        assetPledge = assetPledgeService.getOrCreateFor("USD", Types.MarginType.Initial)
        assetPledge = assetPledgeService.save(assetPledge, 2)

        then:
        assetPledge != null
        def result = assetService.find("USD")
        result.rules.size() == 100
        result.holds.availableQuantity == 100
    }


    void "remove asset quantity after an asset transfer"() {
        given:
        loadAssets(session)


        when:
        def transfer = assetTransferService.create("USD")
        assetTransferService.setCustodianAccount(transfer, "account")

        then:
        def asset = assetService.find("USD")
        asset.rules.size() == 100
        asset.holds.availableQuantity == 100

        when:
        assetService.removeQuantity(asset.assetId, 10.0d)

        then:
        def result = assetService.find("USD")
        result.rules.size() == 100
        result.holds.availableQuantity == 90
    }

    Session createSession() {
        Configuration configuration = new Configuration.Builder()
                .uri(neo4jRule.boltURI().toString())
                .connectionPoolSize(150)
                .encryptionLevel("NONE")
                .build()
        new SessionFactory(configuration, "com.acuo.persist.learning.dummy", "com.acuo.persist.entity")
                .openSession()
    }
}
