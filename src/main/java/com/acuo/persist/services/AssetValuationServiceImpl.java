package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.persist.entity.AssetValuation;
import com.acuo.persist.entity.AssetValue;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Slf4j
public class AssetValuationServiceImpl implements AssetValuationService {

    private final ValuationService valuationService;
    private final ValueService valueService;

    private static final Predicate<AssetValue> assetValuePredicate = assetValue -> assetValue.getValuationDate()
                                                                                    .isBefore(LocalDate.now().minusDays(5));

    @Inject
    public AssetValuationServiceImpl(ValuationService valuationService, ValueService valueService) {
        this.valuationService = valuationService;
        this.valueService = valueService;
    }

    @Override
    public Optional<AssetValue> latest(AssetId assetId) {
        AssetValuation assetValuation = valuationService.getAssetValuationFor(assetId);
        return (assetValuation != null) ? Optional.ofNullable(assetValuation.getLatestValue()) : Optional.empty();
    }

    public AssetValue persist(AssetId assetId, AssetValue value) {

        AssetValuation assetValuation = valuationService.getOrCreateAssetValuationFor(assetId);
        if (assetValuation == null) {
            log.warn("unable to retrieve or create an asset valuation for the asset {}", assetId);
            return value;
        }

        assetValuation.setLatestValue(value);

        deleteOldestValues(assetValuation);

        value.setValuation(assetValuation);

        valuationService.save(assetValuation);

        return valueService.save(value, 1);
    }

    public Collection<AssetValue> persist(List<com.acuo.common.model.results.AssetValuation> valuations) {
        return valuations.stream().map(this::persist).collect(toList());
    }

    public AssetValue persist(com.acuo.common.model.results.AssetValuation valuation) {
        final AssetId assetId = AssetId.fromString(valuation.getAssetId());
        if (log.isDebugEnabled()) {
            log.debug("inserting asset valuation for asset id [{}]", assetId);
        }
        final LocalDateTime valuationDateTime = valuation.getValuationDateTime();
        AssetValue assetValue = createAssetValue(valuation, valuationDateTime);

        assetValue = persist(assetId, assetValue);

        if (log.isDebugEnabled()) {
            log.debug("valuation inserted in the db with valuation date set to {} and timestamp to {}",
                    assetValue.getValuationDate(), assetValue.getTimestamp());
        }
        return assetValue;
    }

    private AssetValue createAssetValue(com.acuo.common.model.results.AssetValuation valuation, LocalDateTime valuationDateTime) {
        AssetValue assetValue = new AssetValue();
        assetValue.setCoupon(valuation.getCoupon());
        assetValue.setNominalCurrency(valuation.getNominalCurrency());
        assetValue.setUnitValue(valuation.getCleanMarketValue());
        assetValue.setPriceQuotationType(valuation.getPriceQuotationType());
        assetValue.setReportCurrency(valuation.getReportCurrency());
        assetValue.setValuationDate(valuationDateTime.toLocalDate());
        assetValue.setTimestamp(valuationDateTime.toInstant(ZoneOffset.UTC));
        assetValue.setYield(valuation.getYield());
        return valueService.save(assetValue);
    }

    private void deleteOldestValues(AssetValuation assetValuation) {
        final Set<AssetValue> values = assetValuation.getValues();
        if (values == null) return;
        final List<AssetValue> toDelete = values.stream()
                .filter(assetValuePredicate)
                .collect(toList());
        if(values.removeAll(toDelete)) {
            assetValuation.setValues(values);
        }
        if(!toDelete.isEmpty()) {
            valueService.delete(toDelete);
        }
    }
}