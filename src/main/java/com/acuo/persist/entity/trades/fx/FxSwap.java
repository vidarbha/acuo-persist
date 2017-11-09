package com.acuo.persist.entity.trades.fx;

import com.acuo.common.ids.TradeId;
import com.acuo.common.model.trade.FxSwapTrade;
import com.acuo.common.model.trade.TradeInfo;
import com.acuo.persist.entity.PricingSource;
import com.acuo.persist.entity.Trade;
import com.acuo.persist.entity.enums.PricingProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.function.Function;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class FxSwap extends Trade<FxSwap> {

    public Function<FxSwap, String> findQuery() { return (swap) -> "";}

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
        final FxSwapTrade fxSwapTrade = new FxSwapTrade();
        fxSwapTrade.setInfo(info);
        fxSwapTrade.setProduct(product);
        return fxSwapTrade;
    }

    @Relationship(type = "NEAR")
    private FxSingle nearLeg;

    @Relationship(type = "FAR")
    private FxSingle farLeg;

}
