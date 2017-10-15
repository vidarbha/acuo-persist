package com.acuo.persist.utils

import com.acuo.common.model.ids.AssetId
import com.acuo.common.model.margin.Types
import com.acuo.persist.builders.FactoryBuilder
import com.acuo.persist.core.ImportService
import com.acuo.persist.entity.enums.Side
import com.acuo.persist.entity.enums.StatementDirection
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

        def client = builder.client(firmId: 999)

        def cpty = builder.client(firmId: 888)

        def clearedAcct = builder.account(accountId: "cleared-acct")

        def bilateralAcct = builder.account(accountId: "cleared-acct")

        def directedTo = builder.entity(legalEntityId: "client-entity", name: "clientEntity", firm: client)

        def sentFrom = builder.entity(legalEntityId: "cpty-entity", name: "cptyEntity",
                custodianAccounts: [bilateralAcct, clearedAcct], firm: cpty)

        def bilateral = bilateral(now, directedTo, sentFrom)
        def cleared = cleared(now, directedTo, sentFrom)

        [bilateral, cleared].each {
            builder.clientSigns(rounding: 10, MTA: 10, threshold: 10, agreement: it, legalEntity: directedTo)
            builder.counterpartSigns(rounding: 9, MTA: 9, agreement: it, legalEntity: sentFrom)
        }

        builder.asset(assetId: AssetId.fromString("USD"))
    }

    def cleared(now, directedTo, sentFrom) {
        def agreement = agreement("cleared-agreement", "cleared")

        builder.statement(statementId: "cleared-statement",
                directedTo: directedTo,
                sentFrom: sentFrom,
                currency: Currency.USD,
                date: now,
                agreement: agreement,
                collaterals: collaterals(now),
                statementItems: calls(now, "c"))
        return agreement
    }

    def bilateral(now, directedTo, sentFrom) {
        def agreement = agreement("bilateral-agreement", "bilateral")

        builder.statement(statementId: "bilateral-statement",
                directedTo: directedTo,
                sentFrom: sentFrom,
                currency: Currency.USD,
                date: now,
                agreement: agreement,
                collaterals: collaterals(now),
                statementItems: calls(now, "b"))

        return agreement
    }

    def agreement(id, type) {
        builder.agreement(agreementId: id,
                currency: Currency.USD,
                notificationTime: LocalTime.of(10, 0),
                type: type,
                tolerance: 10)
    }

    def calls(now, prefix) {
        return builder.list {
            variation(itemId: prefix+"1",
                    ampId: prefix+"1",
                    currency: Currency.USD,
                    side: Side.Client,
                    marginAmount: 1_000_000,
                    marginType: MarginType.Variation,
                    callDate: now.plusDays(1),
                    direction: StatementDirection.IN) {
                step(status: StatementStatus.Expected)
            }
            def matched = variation(itemId: prefix+"2",
                    ampId: prefix+"2",
                    currency: Currency.USD,
                    side: Side.Client,
                    marginAmount: 1_100_000,
                    marginType: MarginType.Variation,
                    callDate: now.plusDays(1),
                    direction: StatementDirection.OUT) {
                step(status: StatementStatus.MatchedToReceived)
            }
            variation(itemId: prefix+"3",
                    ampId: prefix+"3",
                    currency: Currency.USD,
                    side: Side.Cpty,
                    marginAmount: 1_110_000,
                    marginType: MarginType.Variation,
                    callDate: now.plusDays(1),
                    matchedItem: matched,
                    direction: StatementDirection.OUT) {
                step(status: StatementStatus.Unrecon)
            }
        }
    }

    def collaterals(now) {
        return builder.list {
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
    }
}
