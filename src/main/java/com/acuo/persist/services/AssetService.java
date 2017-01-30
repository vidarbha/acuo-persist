package com.acuo.persist.services;

import com.acuo.persist.entity.Asset;
import com.acuo.persist.ids.ClientId;

public interface AssetService extends Service<Asset> {

    Iterable<Asset> findEligibleAssetByClientId(ClientId clientId);

    Iterable<Asset> findReservedAssetByClientId(ClientId clientId);

    Iterable<Asset> findAvailableAssetByClientIdAndCallIds(ClientId clientId, String... callIds);
}
