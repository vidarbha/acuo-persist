package com.acuo.persist.entity.trades.fx;

import com.acuo.common.ids.TradeId;
import com.acuo.common.model.trade.TradeInfo;
import com.acuo.common.model.trade.fx.FxSwapTrade;
import com.acuo.persist.entity.Entity;
import com.acuo.persist.entity.PricingSource;
import com.acuo.persist.entity.Trade;
import com.acuo.persist.entity.enums.PricingProvider;
import com.acuo.persist.entity.trades.AdjustableDate;
import com.acuo.persist.neo4j.converters.CurrencyAmountConverter;
import com.opengamma.strata.basics.currency.CurrencyAmount;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class FxSwap extends Trade<FxSwap> {

    public FxSwap() {}

    public FxSwap(FxSwapTrade model) {
        final TradeInfo info = model.getInfo();
        setTradeId(TradeId.fromString(info.getTradeId()));
        setTradeDate(info.getTradeDate());
        setTradeTime(info.getTradeTime() != null ? info.getTradeTime().toLocalTime() : null);
        setMaturity(info.getMaturityDate());
        setClearingDate(info.getClearedTradeDate());

        PricingSource pricingSource = new PricingSource();
        pricingSource.setName(PricingProvider.Markit);
        setPricingSource(pricingSource);

        final com.acuo.common.model.product.fx.FxSwap product = model.getProduct();
        this.nearLeg = new FxSingle(product.getNearLeg());
        this.farLeg = new FxSingle(product.getFarLeg());
    }

    public FxSwapTrade model() {
        TradeInfo info = new TradeInfo();
        info.setTradeId(getTradeId().toString());
        info.setClearedTradeId(getTradeId().toString());
        info.setClearedTradeDate(getClearingDate());
        info.setTradeDate(getTradeDate());
        info.setBook(getAccount() != null ? getAccount().getAccountId() : null);
        info.setPortfolio(getPortfolio() != null ? getPortfolio().getPortfolioId().toString() : null);

        com.acuo.common.model.product.fx.FxSwap product = new com.acuo.common.model.product.fx.FxSwap();
        product.setNearLeg(nearLeg.model());
        product.setFarLeg(farLeg.model());
        return new FxSwapTrade(info, product);
    }

    @Relationship(type = "NEAR")
    private FxSingle nearLeg;

    @Relationship(type = "FAR")
    private FxSingle farLeg;

    @NodeEntity
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class FxSingle extends Entity<FxSingle> {

        public FxSingle() {}

        public FxSingle(com.acuo.common.model.product.fx.FxSingle leg) {
            setBaseCurrencyAmount(leg.getBaseCurrencyAmount());
            setCounterCurrencyAmount(leg.getCounterCurrencyAmount());
            setPaymentDate(new AdjustableDate(leg.getPaymentDate()));
            setFixingDate(new AdjustableDate(leg.getFixingDate()));
            setNonDeliverable(leg.isNonDeliverable());
        }

        public com.acuo.common.model.product.fx.FxSingle model() {
            com.acuo.common.model.product.fx.FxSingle model = new com.acuo.common.model.product.fx.FxSingle();
            model.setBaseCurrencyAmount(baseCurrencyAmount);
            model.setCounterCurrencyAmount(counterCurrencyAmount);
            model.setPaymentDate(paymentDate.model());
            model.setFixingDate(fixingDate.model());
            model.setNonDeliverable(nonDeliverable);
            return model;
        }

        @Convert(CurrencyAmountConverter.class)
        private CurrencyAmount baseCurrencyAmount;

        @Convert(CurrencyAmountConverter.class)
        private CurrencyAmount counterCurrencyAmount;

        @Relationship(type = "PAYMENT_DATE")
        private AdjustableDate paymentDate;

        @Relationship(type = "FIXING_DATE")
        private AdjustableDate fixingDate;

        private boolean nonDeliverable;
    }
}
