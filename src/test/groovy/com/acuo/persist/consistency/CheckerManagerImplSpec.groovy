package com.acuo.persist.consistency

import com.acuo.common.model.ids.AssetId
import com.acuo.persist.entity.Asset
import com.acuo.persist.entity.AssetTransfer
import com.acuo.persist.modules.ConsistencyModule
import com.opengamma.strata.collect.result.Result
import spock.guice.UseModules
import spock.lang.Specification

import javax.inject.Inject

import static org.assertj.core.api.Assertions.assertThat

@UseModules([ConsistencyModule.class])
class CheckerManagerImplSpec extends Specification {

    @Inject
    private CheckManager checker

    private Asset asset
    private AssetTransfer assetTransfer

    def setup() {
        asset = Stub(Asset.class)
        assetTransfer = Stub(AssetTransfer.class)
    }

    def "balance consistency check returns true for same asset and collateral balance"() {
        given: "a USD cash asset"
        asset.getAssetId() >> AssetId.fromString("USD")

        and: "an asset transfer that always return the USD asset"
        assetTransfer.getOf() >> asset

        when: "we check the consistency"
        Result result = checker.check(assetTransfer)

        then: "we get a success"
        assertThat(result.isSuccess()).isTrue()
    }
}