package com.acuo.persist.validations

import com.acuo.persist.services.AssetPledgeService
import com.acuo.persist.services.CollateralService
import com.opengamma.strata.collect.result.Result
import spock.lang.Specification

import static com.acuo.common.model.margin.Types.AssetType.Cash
import static com.acuo.common.model.margin.Types.BalanceStatus.Pending
import static com.acuo.common.model.margin.Types.BalanceStatus.Settled
import static com.acuo.common.model.margin.Types.MarginType.Variation

class CheckerManagerImplSpec extends Specification {

    def checkerManager
    def assetPledgeService
    def collateralService

    def setup() {
        assetPledgeService = Stub(AssetPledgeService.class)
        collateralService = Stub(CollateralService.class)
        Checker checker = new Checker(assetPledgeService, collateralService)
        checkerManager = new CheckManagerImpl(checker)
    }

    def "consistency check returns true for same asset pledge and collateral balance"() {
        given: "an asset pledge amount of 1.0d"
        assetPledgeService.amount(Cash, Variation, Pending) >> 1.0d

        and: "a collateral balance of 1.0d"
        collateralService.amount(Cash, Variation, Pending) >> 1.0d

        when: "we check the validations"
        def results = checkerManager.check()

        then: "we get 8 success results with one of 1.0d and the other of 0.0d"
        results.find { it.getValue() == 1.0d } == Result.success(1.0d)
        results.count { it.getValue() == 1.0d } == 1
        results.count { it.getValue() == 0.0d } == 7
    }

    def "consistency check returns a failure for different amounts for the same asset pledge and collateral balance"() {
        given: "an asset pledge amount of 1.0d"
        assetPledgeService.amount(Cash, Variation, Pending) >> 1.0d

        and: "a collateral balance of 2.0d"
        collateralService.amount(Cash, Variation, Pending) >> 2.0d

        when: "we check the validations"
        def results = checkerManager.check()

        then: "we get 1 failure and 7 success results"
        results.count { it.isFailure() } == 1
        results.count { it.isSuccess() } == 7
    }

    def "consistency check returns two failures for amounts set on different asset pledge and collateral balance"() {
        given: "an asset pledge amount of 1.0d"
        assetPledgeService.amount(Cash, Variation, Pending) >> 1.0d

        and: "a collateral balance of 2.0d"
        collateralService.amount(Cash, Variation, Settled) >> 1.0d

        when: "we check the validations"
        def results = checkerManager.check()

        then: "we get 1 failure and 7 success results"
        results.count { it.isFailure() } == 2
        results.count { it.isSuccess() } == 6
    }
}