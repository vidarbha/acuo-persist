package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.persist.entity.Settlement;

public interface SettlementService extends Service<Settlement, String> {

    Settlement getSettlementFor(AssetId assetId);

    Settlement getOrCreateSettlementFor(AssetId assetId);
}
