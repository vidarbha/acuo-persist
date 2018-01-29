package com.acuo.persist.entity.trades.fx;

import com.acuo.common.model.trade.FxSwapTrade;
import com.acuo.persist.entity.PricingSource;
import com.acuo.persist.entity.trades.Trade;
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
        super(model);

        PricingSource pricingSource = new PricingSource();
        pricingSource.setName(PricingProvider.Markit);
        setPricingSource(pricingSource);

        final com.acuo.common.model.product.fx.FxSwap product = model.getProduct();
        this.nearLeg = new FxSingle(product.getNearLeg());
        this.farLeg = new FxSingle(product.getFarLeg());
    }

    public FxSwapTrade model() {
        final FxSwapTrade fxSwapTrade = new FxSwapTrade();
        fxSwapTrade.setInfo(info());

        com.acuo.common.model.product.fx.FxSwap product = new com.acuo.common.model.product.fx.FxSwap();
        product.setNearLeg(nearLeg.model());
        product.setFarLeg(farLeg.model());
        fxSwapTrade.setProduct(product);

        return fxSwapTrade;
    }

    @Relationship(type = "NEAR")
    private FxSingle nearLeg;

    @Relationship(type = "FAR")
    private FxSingle farLeg;

}
