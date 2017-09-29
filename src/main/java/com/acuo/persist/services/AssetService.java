package com.acuo.persist.services;

import com.acuo.persist.entity.Asset;
import com.acuo.common.model.ids.AssetId;
import com.acuo.common.model.ids.ClientId;
import com.acuo.persist.entity.AssetPledge;
import com.acuo.persist.entity.AssetTransfer;

public interface AssetService extends Service<Asset, AssetId>  {

    Iterable<Asset> findAvailableAssetByClientId(ClientId clientId);

    Iterable<Asset> findAvailableAssetByClientIdAndCallId(ClientId clientId, String callId);

    Double totalHaircut(AssetId assetId, String callId);

}
