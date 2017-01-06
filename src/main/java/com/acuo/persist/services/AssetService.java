package com.acuo.persist.services;

import com.acuo.persist.entity.Asset;

public interface AssetService extends Service<Asset> {

    Iterable<Asset> findEligibleAssetByClientId(String clientId);

    Iterable<Asset> findReservedAssetByClientId(String clientId);
}
