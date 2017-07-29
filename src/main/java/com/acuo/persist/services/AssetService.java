package com.acuo.persist.services;

import com.acuo.persist.entity.Asset;
import com.acuo.persist.ids.ClientId;

public interface AssetService extends Service<Asset, String>  {

    Iterable<Asset> findAvailableAssetByClientId(ClientId clientId);

    Iterable<Asset> findAvailableAssetByClientIdAndCallId(ClientId clientId, String callId);
}
