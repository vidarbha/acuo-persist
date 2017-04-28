package com.acuo.persist.services;

import com.acuo.persist.entity.AssetTransfer;
import org.neo4j.ogm.model.Result;

public interface AssetTransferService extends Service<AssetTransfer, String> {

    Result getPledgedAssets();
}
