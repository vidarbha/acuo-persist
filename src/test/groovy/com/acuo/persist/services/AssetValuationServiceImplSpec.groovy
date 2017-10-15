package com.acuo.persist.services

import com.acuo.common.model.ids.AssetId
import com.acuo.persist.entity.Asset
import com.acuo.persist.entity.AssetValuation
import com.acuo.persist.entity.AssetValue
import org.neo4j.ogm.session.Session
import spock.lang.Specification
import spock.lang.Subject

import javax.inject.Provider
import java.time.LocalDate

class AssetValuationServiceImplSpec extends Specification {

    @Subject
    AssetValuationService subject

    Provider<Session> provider = Mock()
    AssetService assetService = Mock()
    ValueService valueService = Mock()

    void setup() {
        subject =  new AssetValuationServiceImpl(provider, assetService, valueService)
    }

    def "persist a new asset value and not deleting a previous value with 5 days"() {
        given:
        def assetId = AssetId.fromString("TEST")
        Session session = Mock()
        Asset asset = Mock()
        AssetValue value = Mock()
        AssetValue oldValue = Mock()
        AssetValuation valuation = Mock()

        when:
        subject.persist(assetId, value)

        then:
        provider.get() >> session
        assetService.find(assetId) >> asset
        1 * session.queryForObject( *_) >> valuation
        1 * valuation.setLatestValue(value)
        1 * valuation.getValues() >> [value, oldValue]
        1 * value.getValuationDate() >> LocalDate.now()
        1 * oldValue.getValuationDate() >> LocalDate.now().minusDays(4)
        0 * valuation.setValues([value])
        0 * valueService.delete(_)
        1 * value.setValuation(valuation)
        1 * session.save(valuation, 2)
        1 * valueService.save(value, 2)
    }

    def "persist a new asset value and delete the ones older than 5 days"() {
        given:
        def assetId = AssetId.fromString("TEST")
        Session session = Mock()
        Asset asset = Mock()
        AssetValue value = Mock()
        AssetValue oldValue = Mock()
        AssetValuation valuation = Mock()

        when:
        subject.persist(assetId, value)

        then:
        provider.get() >> session
        assetService.find(assetId) >> asset
        1 * session.queryForObject( *_) >> valuation
        1 * valuation.setLatestValue(value)
        1 * valuation.getValues() >> [value, oldValue]
        1 * value.getValuationDate() >> LocalDate.now()
        1 * oldValue.getValuationDate() >> LocalDate.now().minusDays(6)
        1 * valuation.setValues({ it -> it.size() == 1 && it.contains(value)})
        1 * valueService.delete([oldValue])
        1 * value.setValuation(valuation)
        1 * session.save(valuation, 2)
        1 * valueService.save(value, 2)
    }
}
