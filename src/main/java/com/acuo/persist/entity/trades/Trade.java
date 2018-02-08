package com.acuo.persist.entity.trades;

import com.acuo.common.cache.manager.CacheManager;
import com.acuo.common.cache.manager.Cacheable;
import com.acuo.common.cache.manager.CachedObject;
import com.acuo.common.ids.TradeId;
import com.acuo.common.model.trade.ProductTrade;
import com.acuo.common.model.trade.TradeInfo;
import com.acuo.common.util.ArgChecker;
import com.acuo.persist.entity.Entity;
import com.acuo.persist.entity.Portfolio;
import com.acuo.persist.entity.PricingSource;
import com.acuo.persist.entity.ServiceError;
import com.acuo.persist.entity.TradingAccount;
import com.acuo.persist.neo4j.converters.CurrencyConverter;
import com.acuo.persist.neo4j.converters.LocalDateConverter;
import com.acuo.persist.neo4j.converters.LocalTimeConverter;
import com.google.common.collect.ImmutableList;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.date.HolidayCalendarId;
import com.opengamma.strata.basics.date.HolidayCalendars;
import com.opengamma.strata.basics.date.ImmutableHolidayCalendar;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
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
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

@NodeEntity
@Data
@EqualsAndHashCode(callSuper = false, exclude = {"id", "account", "errors"})
@ToString(exclude = {"account", "errors"})
@Slf4j
public abstract class Trade<T extends Trade> implements Entity<T>, Comparable<T> {

    private static CacheManager cacheManager = new CacheManager();

    protected static String TRADE_TYPE_BILATERAL = "Bilateral";

    public Function<T, String> findQuery() { return (swap) -> "";}

    public Trade() {}

    public Trade(ProductTrade model) {
        final TradeInfo info = model.getInfo();
        setTradeId(TradeId.fromString(info.getTradeId()));
        setTradeDate(info.getTradeDate());
        setEffectiveDate(info.getEffectiveDate());
        setTradeTime(info.getTradeTime() != null ? info.getTradeTime().toLocalTime() : null);
        setMaturity(info.getMaturityDate());
        setClearingDate(info.getClearedTradeDate());
        setTradeType(info.getDerivativeType());
    }

    protected TradeInfo info() {
        TradeInfo info = new TradeInfo();
        info.setTradeId(getTradeId().toString());
        info.setClearedTradeId(getTradeId().toString());
        info.setClearedTradeDate(getClearingDate());
        info.setTradeDate(getTradeDate());
        info.setEffectiveDate(getEffectiveDate());
        info.setBook(getAccount() != null ? getAccount().getAccountId() : null);
        info.setPortfolio(getPortfolio() != null ? getPortfolio().getPortfolioId().toString() : null);

        return info;
    }

    protected HolidayCalendarId holidays(Leg leg) {
        String refCalendar = leg.getRefCalendar();
        Cacheable value = cacheManager.getCache(ArgChecker.notNull(refCalendar, "refCalendar"));
        if (value == null) {
            HolidayCalendarId holidays;
            try {
                holidays = HolidayCalendars.of(refCalendar).getId();
            } catch (Exception e) {
                log.warn(e.getMessage());
                holidays = ImmutableHolidayCalendar.of(HolidayCalendarId.of(refCalendar), ImmutableList.of(), SATURDAY, SUNDAY).getId();
            }
            value = new CachedObject(holidays, refCalendar, 0);
            cacheManager.putCache(value);
        }
        return (HolidayCalendarId)value.getObject();
    }

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "id")
    @Id
    @Convert(TradeIdConverter.class)
    protected TradeId tradeId;

    @Property(name = "acct")
    private String acct;

    private String tradeType;

    private String underlyingAssetId;

    private Double notional;

    private String buySellProtection;

    private Double couponRate;

    @Convert(LocalDateConverter.class)
    private LocalDate tradeDate;

    @Convert(LocalDateConverter.class)
    private LocalDate effectiveDate;

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
