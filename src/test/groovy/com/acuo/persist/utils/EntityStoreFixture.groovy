package com.acuo.persist.utils

import com.acuo.common.ids.AssetId
import com.acuo.common.model.margin.Types
import com.acuo.common.util.LocalDateUtils
import com.acuo.persist.builders.FactoryBuilder
import com.acuo.persist.core.DataImporter
import com.acuo.persist.entity.enums.Side
import com.acuo.persist.entity.enums.StatementDirection
import com.acuo.persist.entity.enums.StatementStatus
import com.opengamma.strata.basics.currency.Currency

import javax.inject.Inject
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

import static com.acuo.common.model.margin.Types.MarginType

class EntityStoreFixture {

    final FactoryBuilder builder
    final DataImporter importService

    @Inject
    EntityStoreFixture(FactoryBuilder builder, DataImporter importService){
        this.builder = builder
        this.importService = importService
    }

    void install() {
        importService.deleteAll()

        def now = LocalDate.now()

        def clearedAcct = builder.account(accountId: "clearedAcct", name: "clearedAcct", region: "SG") {
            custodian(custodianId: "GS")
        }

        def bilateralAcct = builder.account(accountId: "bilateralAcct", name: "bilateralAcct", region: "SG") {
            custodian(custodianId: "JPM")
        }

        def client = builder.client(firmId: 999, custodianAccounts: [clearedAcct, bilateralAcct])

        def cpty = builder.client(firmId: 888)


        def directedTo = builder.entity(legalEntityId: "clientEntity", name: "clientEntity",
                custodianAccounts: [bilateralAcct, clearedAcct], firm: client)

        def sentFrom = builder.entity(legalEntityId: "cptyEntity", name: "cptyEntity",
                custodianAccounts: [bilateralAcct, clearedAcct], firm: cpty)

        def clearedRules = [rule()]
        def usd = builder.asset(
                assetId: AssetId.fromString("USD"),
                name: "USD",
                currency: Currency.USD,
                idType: "CASH",
                ACUOCategory: "CASH",
                ICADCode: "US-CASH",
                type: "CASH",
                rules: clearedRules,
                parValue: 1.0d,
                minUnit: 1.0d,
                yield: 0,
                issueDate: now,
                maturityDate: now) {
            settlement(settlementId: "s1") {
                settlementDate(queryDateTime: LocalDateTime.now(),
                        creationDateTime: LocalDateTime.now(),
                        settlementDate: LocalDateUtils.valuationDate())
            }
            holds(custodianAccount: clearedAcct, availableQuantity: 10_000_000, internalCost: 1.0, opptCost: 1.0)
        }
        def cleared = cleared(now, directedTo, sentFrom, clearedRules)

        def bilateralRules = [rule()]
        def jpy = builder.asset(
                assetId: AssetId.fromString("JPY"),
                name: "JPY",
                currency: Currency.JPY,
                idType: "CASH",
                ACUOCategory: "CASH",
                ICADCode: "JPY-CASH",
                type: "CASH",
                rules: bilateralRules,
                parValue: 1.0d,
                minUnit: 1.0d,
                yield: 0,
                issueDate: now,
                maturityDate: now) {
            settlement(settlementId: "s2") {
                settlementDate(queryDateTime: LocalDateTime.now(),
                        creationDateTime: LocalDateTime.now(),
                        settlementDate: LocalDateUtils.valuationDate())
            }
            holds(custodianAccount: bilateralAcct, availableQuantity: 10_000_000, internalCost: 1.0, opptCost: 1.0)
        }
        def bilateral = bilateral(now, directedTo, sentFrom, bilateralRules)

        [bilateral, cleared].each {
            builder.clientSigns(rounding: 10, MTA: 10, threshold: 10, agreement: it, legalEntity: directedTo)
            builder.counterpartSigns(rounding: 9, MTA: 9, agreement: it, legalEntity: sentFrom)
        }

    }

    def cleared(now, directedTo, sentFrom, rules) {
        def agreement = agreement("cleared-agreement", "cleared", rules)

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

    def bilateral(now, directedTo, sentFrom, rules) {
        def agreement = agreement("bilateral-agreement", "bilateral", rules)

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

    def agreement(id, type, rules) {
        builder.agreement(agreementId: id,
                currency: Currency.USD,
                notificationTime: LocalTime.of(10, 0),
                type: type,
                tolerance: 10,
                rules: rules)
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
                    direction: StatementDirection.IN,
                    fxRate: 1) {
                step(status: StatementStatus.Expected)
            }
            def matched = variation(itemId: prefix+"2",
                    ampId: prefix+"2",
                    currency: Currency.USD,
                    side: Side.Client,
                    marginAmount: 1_100_000,
                    marginType: MarginType.Variation,
                    callDate: now.plusDays(1),
                    direction: StatementDirection.OUT,
                    fxRate: 1) {
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
                    direction: StatementDirection.OUT,
                    fxRate: 1) {
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

    def rule() {
        return builder.rule(marginTypes: [MarginType.Variation],
                haircut: 0,
                fxHaircut: 0,
                externalCost: 0,
                interestRate: 0,
                fxRate: 0
        )
    }
}
