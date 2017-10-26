package com.acuo.persist.services;

import com.acuo.common.ids.AssetId;
import com.acuo.persist.entity.AssetValuation;
import com.acuo.persist.entity.AssetValue;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AssetValuationService  extends Service<AssetValuation, Long> {

    AssetValuation getAssetValuationFor(AssetId assetId);

    AssetValuation getOrCreateAssetValuationFor(AssetId assetId);

    AssetValue persist(AssetId assetId, AssetValue value);

    Collection<AssetValue> persist(List<com.acuo.common.model.results.AssetValuation> valuations);

    AssetValue persist(com.acuo.common.model.results.AssetValuation valuation);

    Optional<AssetValue> latest(AssetId assetId);
}