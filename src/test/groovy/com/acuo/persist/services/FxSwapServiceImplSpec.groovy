package com.acuo.persist.services

import com.acuo.common.ids.ClientId
import com.acuo.common.model.AdjustableDate
import com.acuo.common.model.BusinessDayAdjustment
import com.acuo.common.model.product.fx.FxSingle
import com.acuo.common.model.trade.FxSwapTrade
import com.acuo.common.model.trade.TradeInfo
import com.acuo.persist.core.DataImporter
import com.acuo.persist.entity.trades.Trade
import com.acuo.persist.entity.trades.fx.FxSwap
import com.acuo.persist.modules.ConfigurationTestModule
import com.acuo.persist.modules.ImportTestServiceModule
import com.acuo.persist.modules.InProcessNeo4jServerModule
import com.acuo.persist.modules.RepositoryModule
import com.opengamma.strata.basics.currency.Currency
import com.opengamma.strata.basics.currency.CurrencyAmount
import spock.guice.UseModules
import spock.lang.Specification
import spock.lang.Subject

import javax.inject.Inject
import java.time.LocalDate
import java.time.Period

import static com.opengamma.strata.basics.date.BusinessDayConventions.MODIFIED_FOLLOWING
import static com.opengamma.strata.basics.date.HolidayCalendarIds.GBLO

@UseModules([
        ConfigurationTestModule,
        InProcessNeo4jServerModule,
        ImportTestServiceModule,
        RepositoryModule
])
class FxSwapServiceImplSpec extends Specification {

    @Inject
    DataImporter importService

    @Inject
    TradingAccountService accountService

    @Subject
    @Inject
    TradeService<Trade> tradeService

    ClientId client999 = ClientId.fromString("999")

    void setup() {
        importService.reload()
    }

    void "Compare an fx swap trade with its persisted version"(){
        given:
        FxSwapTrade original = fxSwapTrade()
        FxSwap entity = new FxSwap(original)
        entity.setAccount(accountService.account(client999, "ACUOSG8745"))

        when:
        tradeService.createOrUpdate(client999, [entity])

        and:
        FxSwap persisted = tradeService.findTradeBy(client999, entity.tradeId) as FxSwap

        then:
        persisted != null

        when:
        FxSwapTrade converted = persisted.model()

        then:
        converted == original
    }

    private static FxSwapTrade fxSwapTrade() {
        TradeInfo info = new TradeInfo()
        info.setTradeId("dummyFxSwap")
        info.setClearedTradeId("dummyFxSwap")
        info.setBook("ACUOSG8745")

        com.acuo.common.model.product.fx.FxSwap product = new com.acuo.common.model.product.fx.FxSwap()
        FxSingle nearLeg = fxSingle(1_000_000, 1_200_000, Period.ofMonths(6) )
        product.setNearLeg(nearLeg)

        FxSingle farLeg = fxSingle(1_000_000, 1_200_000, Period.ofYears(1) )
        product.setFarLeg(farLeg)

        return new FxSwapTrade(info: info, product: product)
    }

    private static FxSingle fxSingle(double baseAmount, double counterAmount, Period payPeriod) {
        FxSingle single = new FxSingle()
        single.setBaseCurrencyAmount(CurrencyAmount.of(Currency.USD, baseAmount))
        single.setCounterCurrencyAmount(CurrencyAmount.of(Currency.EUR, counterAmount))
        BusinessDayAdjustment dayAdjustment = new BusinessDayAdjustment(businessDayConvention: MODIFIED_FOLLOWING, holidays: [GBLO])
        def payDate = LocalDate.now().plus(payPeriod)
        AdjustableDate paymentDate = new AdjustableDate(date: payDate, adjustment: dayAdjustment)
        AdjustableDate fixingDate = new AdjustableDate(date: payDate.minusDays(2), adjustment: dayAdjustment)
        single.setPaymentDate(paymentDate)
        single.setMaturityDate(fixingDate)
        return single
    }
}
