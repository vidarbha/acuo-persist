package com.acuo.persist.services;

import com.acuo.persist.entity.AssetTransfer;
import com.acuo.persist.ids.ClientId;
import org.neo4j.ogm.model.Result;

public interface AssetTransferService extends Service<AssetTransfer, String> {

    Iterable<AssetTransfer> findArrivingAssetTransferByClientId(ClientId clientId);

    Iterable<AssetTransfer> findDepartedAssetTransferByClientId(ClientId clientId);

    void sendAsset(String marginCallId, String assetId, Double quantity, String fromAccount);

    void receiveAsset(String marginCallId, String assetId, Double quantity, String fromAccount);

    Result getPledgedAssets();
}
