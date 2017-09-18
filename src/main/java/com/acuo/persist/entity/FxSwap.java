package com.acuo.persist.entity;

import com.acuo.common.model.ids.TradeId;
import com.acuo.persist.neo4j.converters.CurrencyAmountConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.TypedStringConverter;
import com.opengamma.strata.basics.currency.CurrencyAmount;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.time.LocalDate;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = true)
public class FxSwap extends Entity<FxSwap>{

    @Property(name = "id")
    @Index(primary = true)
    @Convert(TypedStringConverter.TradeIdConverter.class)
    protected TradeId tradeId;

    @Relationship(type = "NEAR")
    private FxSingle nearLeg;

    @Relationship(type = "FAR")
    private FxSingle farLeg;

    @NodeEntity
    @Data
    public static class FxSingle {

        @Convert(CurrencyAmountConverter.class)
        private CurrencyAmount baseCurrencyAmount;

        @Convert(CurrencyAmountConverter.class)
        private CurrencyAmount counterCurrencyAmount;

        @Convert(LocalDateConverter.class)
        private LocalDate paymentDate;
    }
}
