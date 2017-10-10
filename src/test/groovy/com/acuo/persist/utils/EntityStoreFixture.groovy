package com.acuo.persist.utils

import com.acuo.common.model.margin.Types
import com.acuo.persist.builders.FactoryBuilder
import com.acuo.persist.core.ImportService
import com.acuo.persist.entity.enums.Side
import com.acuo.persist.entity.enums.StatementStatus
import com.opengamma.strata.basics.currency.Currency

import javax.inject.Inject
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

import static com.acuo.common.model.margin.Types.MarginType

class EntityStoreFixture {

    final FactoryBuilder builder
    final ImportService importService

    @Inject
    EntityStoreFixture(FactoryBuilder builder, ImportService importService){
        this.builder = builder
        this.importService = importService
    }

    void install() {
        importService.deleteAll()

        def now = LocalDate.now()

        def account = builder.account(accountId: "act1")

        def sentFrom = builder.entity(legalEntityId: "l2", custodianAccounts: [account])

        def a1 = builder.agreement(agreementId: "a1",
                currency: Currency.USD,
                notificationTime: LocalTime.of(10,0),
                type: "bilateral",
                tolerance: 10)

        def collaterals = builder.list {
            collateral(marginType: MarginType.Initial,
                    assetType: Types.AssetType.Cash,
                    status: Types.BalanceStatus.Pending) {
                collateralValue(amount: 1_000_000,
                        source: "Fixture",
                        valuationDate: now,
                        timestamp: Instant.now())
                collateralValue(amount: 1_010_000,
                        source: "Fixture",
                        valuationDate: now,
                        timestamp: Instant.now())
            }
            collateral(marginType: MarginType.Variation,
                    assetType: Types.AssetType.NonCash,
                    status: Types.BalanceStatus.Settled) {
                collateralValue(amount: 1_000_000,
                        source: "Fixture",
                        valuationDate: now,
                        timestamp: Instant.now(),)
                collateralValue(amount: 1_010_000,
                        source: "Fixture",
                        valuationDate: now,
                        timestamp: Instant.now(),)
            }
        }

        def statement = builder.statement(statementId: "ms1",
                sentFrom: sentFrom,
                currency: Currency.USD,
                date: now,
                agreement: a1,
                collaterals: collaterals) {
            variation(itemId: "c1",
                    ampId: "123456789",
                    currency: Currency.USD,
                    side: Side.Client,
                    marginAmount: 1_000_000,
                    marginType: MarginType.Variation,
                    callDate: now.plusDays(1)) {
                step(status: StatementStatus.Expected)
            }
            def c2 = variation(itemId: "c2",
                        ampId: "0000000",
                        currency: Currency.USD,
                        side: Side.Client,
                        marginAmount: 1_100_000,
                        marginType: MarginType.Variation,
                        callDate: now.plusDays(1)) {
                step(status: StatementStatus.MatchedToReceived)
            }
            variation(itemId: "c3",
                        ampId: "0000000",
                        currency: Currency.USD,
                        side: Side.Cpty,
                        marginAmount: 1_110_000,
                        marginType: MarginType.Variation,
                        callDate: now.plusDays(1),
                        matchedItem: c2) {
                step(status: StatementStatus.Unrecon)
            }
        }

        builder.client(firmId: 999) {
            entity(legalEntityId: "l1", name: "clientEntity")
        }

        builder.client(firmId: 888) {
            entity(legalEntityId: "l2", name: "cptyEntity")
        }

        builder.clientSigns(rounding: 10, MTA: 10, threshold:10, agreement: a1) {
            entity(legalEntityId: "l1")
        }

        builder.counterpartSigns(rounding: 9, MTA: 9, agreement: a1) {
            entity(legalEntityId: "l2")
        }
    }
}
