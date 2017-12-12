package com.acuo.persist.entity;

import com.acuo.common.ids.TradeId;
import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalTimeConverter;
import com.opengamma.strata.basics.currency.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.acuo.persist.neo4j.converters.TypedStringConverter.TradeIdConverter;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"account", "errors"})
@ToString(exclude = {"account", "errors"})
public abstract class Trade<T extends Trade> extends Entity<T> implements Comparable<T> {

    public Function<T, String> findQuery() { return (swap) -> "";}

    @Property(name = "id")
    @Index(primary = true)
    @Convert(TradeIdConverter.class)
    protected TradeId tradeId;

    private String tradeType;

    private String underlyingAssetId;

    private Double notional;

    private String buySellProtection;

    private Double couponRate;

    @Convert(LocalDateConverter.class)
    private LocalDate tradeDate;

    @Convert(LocalTimeConverter.class)
    private LocalTime tradeTime;

    @Convert(LocalDateConverter.class)
    private LocalDate maturity;

    @Convert(LocalDateConverter.class)
    private LocalDate clearingDate;

    @Convert(CurrencyConverter.class)
    private Currency currency;

    private String underlyingEntity;

    private Double factor;

    private String seniority;

    @Relationship(type = "POSITIONS_ON", direction = Relationship.INCOMING)
    private TradingAccount account;

    @Relationship(type = "BELONGS_TO")
    private Portfolio portfolio;

    @Relationship(type = "PRICING_SOURCE")
    private PricingSource pricingSource;

    @Relationship(type = "ENCOUNTERS")
    private List<ServiceError> errors = new ArrayList<>();

    public void addErrors(ServiceError error) {
        errors.add(error);
    }

    public void addAllErrors(List<ServiceError> error) {
        errors.addAll(errors);
    }

    @Override
    public int compareTo(@Nonnull T o) {
        return this.getTradeId().compareTo(o.getTradeId());
    }
}
