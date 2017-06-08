package com.acuo.persist.utils;

import com.acuo.common.model.results.AssetValuation;
import com.acuo.persist.ids.AssetId;
import com.acuo.persist.services.AssetService;
import com.acuo.persist.services.AssetValuationService;
import com.opengamma.strata.basics.currency.Currency;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class ValuationHelper {

    private final AssetValuationService assetValuationService;
    private final AssetService assetService;

    @Inject
    public ValuationHelper(AssetValuationService assetValuationService, AssetService assetService) {
        this.assetValuationService = assetValuationService;
        this.assetService = assetService;
    }

    public void createDummyAssetValues() {
        assetValuations().forEach(assetValuationService::persist);
    }

    public void createAssetValue(Currency currency) {
        AssetValuation valuation = new AssetValuation();
        valuation.setAssetId(currency.getCode());
        valuation.setNotional(1d);
        valuation.setCleanMarketValue(1d);
        valuation.setNominalCurrency(currency);
        valuation.setReportCurrency(currency);
        valuation.setSource("Reuters");
        valuation.setValuationDateTime(LocalDateTime.now());
        assetValuationService.persist(valuation);
    }

    private List<AssetValuation> assetValuations() {
        return assetIds().stream()
                .map(id -> {
                    AssetValuation assetValuation = new AssetValuation();
                    assetValuation.setAssetId(id.toString());
                    assetValuation.setIdType("ISIN");
                    assetValuation.setYield(0.0d);
                    assetValuation.setCleanMarketValue(1.0d);
                    assetValuation.setNotional(1.0d);
                    assetValuation.setValuationDateTime(LocalDateTime.now());
                    assetValuation.setPriceQuotationType("TEST");
                    assetValuation.setSource("UNIT TEST");
                    assetValuation.setNominalCurrency(Currency.USD);
                    assetValuation.setReportCurrency(Currency.USD);
                    assetValuation.setCoupon(0.0d);
                    return assetValuation;
                })
                .collect(toList());
    }

    private List<AssetId> assetIds() {
        return StreamSupport.stream(assetService.findAll().spliterator(), true)
                .map(asset -> AssetId.fromString(asset.getAssetId()))
                .collect(toList());
    }
}
