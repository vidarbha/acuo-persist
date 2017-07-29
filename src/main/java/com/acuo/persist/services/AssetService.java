package com.acuo.persist.services;

import com.acuo.persist.entity.Asset;
import com.acuo.common.model.ids.AssetId;
import com.acuo.common.model.ids.ClientId;

public interface AssetService extends Service<Asset, AssetId>  {

    Iterable<Asset> findAvailableAssetByClientId(ClientId clientId);

    Iterable<Asset> findAvailableAssetByClientIdAndCallId(ClientId clientId, String callId);
}
