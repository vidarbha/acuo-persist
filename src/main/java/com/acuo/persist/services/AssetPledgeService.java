package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.AssetPledge;
import com.acuo.persist.entity.AssetTransfer;

public interface AssetPledgeService extends Service<AssetPledge, Long> {

    AssetPledge getFor(AssetId assetId,
                       Types.MarginType marginType,
                       Types.BalanceStatus status);

    AssetPledge getOrCreateFor(AssetId assetId,
                               Types.MarginType marginType,
                               Types.BalanceStatus status);

    AssetPledge handle(AssetTransfer transfer);

    Double amount(Types.MarginType[] types, Types.BalanceStatus[] statuses);

}
