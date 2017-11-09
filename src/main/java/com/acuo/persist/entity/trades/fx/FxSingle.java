package com.acuo.persist.entity.trades.fx;

import com.acuo.persist.entity.Entity;
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
@EqualsAndHashCode(callSuper = false)
public class FxSingle extends Entity<FxSingle> {

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
