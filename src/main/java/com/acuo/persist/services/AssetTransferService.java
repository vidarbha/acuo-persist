package com.acuo.persist.services;

import com.acuo.common.ids.AssetId;
import com.acuo.common.ids.ClientId;
import com.acuo.persist.entity.AssetTransfer;
import com.acuo.persist.entity.enums.AssetTransferStatus;
import org.neo4j.ogm.model.Result;

public interface AssetTransferService extends Service<AssetTransfer, String> {

    Iterable<AssetTransfer> findAssetTransferByClientIdAndStatus(ClientId clientId, AssetTransferStatus status);

    void sendAsset(String marginCallId, AssetId assetId, Double quantity, String fromAccount);

    void receiveAsset(String marginCallId, AssetId assetId, Double quantity, String fromAccount);

    Result getPledgedAssets();
}
