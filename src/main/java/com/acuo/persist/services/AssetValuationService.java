package com.acuo.persist.services;

import com.acuo.common.model.results.AssetValuation;
import com.acuo.persist.entity.AssetValue;
import com.acuo.common.model.ids.AssetId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AssetValuationService {

    Optional<AssetValue> latest(AssetId assetId);

    AssetValue persist(AssetId assetId, AssetValue value);

    Collection<AssetValue> persist(List<AssetValuation> valuations);

    AssetValue persist(AssetValuation valuation);
}