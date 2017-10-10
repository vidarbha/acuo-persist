package com.acuo.persist.services

import com.acuo.common.model.ids.AssetId
import com.acuo.persist.entity.AssetValuation
import com.acuo.persist.entity.AssetValue
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class AssetValuationServiceImplSpec extends Specification {

    @Subject
    AssetValuationService subject

    ValuationService valuationService = Mock()
    ValueService valueService = Mock()

    void setup() {
        subject =  new AssetValuationServiceImpl(valuationService, valueService)
    }

    def "persist a new asset value"() {
        given:
        def assetId = AssetId.fromString("TEST")
        AssetValue value = Mock()
        AssetValuation valuation = Mock()

        when:
        subject.persist(assetId, value)

        then:
        1 * valuationService.getOrCreateAssetValuationFor(assetId) >> valuation
        1 * valuation.setLatestValue(value)
        1 * valuation.getValues() >> [value]
        1 * value.getValuationDate() >> LocalDate.now()
        0 * valueService.delete(_)
        1 * value.setValuation(valuation)
        1 * valueService.save(value, 1)
    }

    def "persist a new asset value and delete the ones older than 5 days"() {
        given:
        def assetId = AssetId.fromString("TEST")
        AssetValue value = Mock()
        AssetValuation valuation = Mock()

        when:
        subject.persist(assetId, value)

        then:
        1 * valuationService.getOrCreateAssetValuationFor(assetId) >> valuation
        1 * valuation.setLatestValue(value)
        1 * valuation.getValues() >> [value]
        1 * value.getValuationDate() >> LocalDate.now().minusDays(6)
        1 * valueService.delete(_)
        1 * value.setValuation(valuation)
        1 * valueService.save(value, 1)
    }
}
