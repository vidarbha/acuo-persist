package com.acuo.persist.services;

import com.acuo.persist.entity.AssetValuation;
import com.acuo.persist.entity.AssetValue;
import com.acuo.persist.ids.AssetId;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Slf4j
public class AssetValuationServiceImpl implements AssetValuationService {

    private final ValuationService valuationService;
    private final ValueService valueService;

    @Inject
    public AssetValuationServiceImpl(ValuationService valuationService, ValueService valueService) {
        this.valuationService = valuationService;
        this.valueService = valueService;
    }

    public AssetValue persist(AssetId assetId, AssetValue value) {

        AssetValuation assetValuation = valuationService.getOrCreateAssetValuationFor(assetId);
        if (assetValuation == null) {
            log.warn("unable to retrieve or create an asset valuation for the asset {}", assetId);
            return value;
        }

        deleteLatestValue(assetValuation);

        value.setValuation(assetValuation);

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
            log.debug("valuation inserted in the db with timestamp set to {}", assetValue.getDateTime());
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
        assetValue.setDateTime(valuationDateTime);
        assetValue.setYield(valuation.getYield());
        return assetValue;
    }

    private void deleteLatestValue(AssetValuation assetValuation) {
        final Set<AssetValue> values = assetValuation.getValues();
        if (values == null) return;
        valueService.delete(new ArrayList(values));
        assetValuation.setValues(null);
    }
}