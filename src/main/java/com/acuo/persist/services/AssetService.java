package com.acuo.persist.services;

import com.acuo.common.ids.AssetId;
import com.acuo.common.ids.ClientId;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.SettlementDate;

import java.util.Optional;

public interface AssetService extends Service<Asset, AssetId>  {

    Iterable<Asset> findAssets(ClientId clientId);

    Iterable<Asset> findAvailableAssetByClientId(ClientId clientId);

    Iterable<Asset> findAvailableAssetByClientIdAndCallId(ClientId clientId, String callId);

    Double totalHaircut(AssetId assetId, String callId);

    Optional<SettlementDate> settlementDate(AssetId assetId);

}
