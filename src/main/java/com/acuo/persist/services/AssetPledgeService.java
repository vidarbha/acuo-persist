package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.AssetPledge;
import com.acuo.persist.entity.AssetTransfer;

import static com.acuo.common.model.margin.Types.*;

public interface AssetPledgeService extends Service<AssetPledge, Long> {

    AssetPledge getFor(AssetId assetId,
                       MarginType marginType,
                       BalanceStatus status);

    AssetPledge getOrCreateFor(AssetId assetId,
                               MarginType marginType,
                               BalanceStatus status);

    AssetPledge handle(AssetTransfer transfer);

    Double sum(MarginType[] types, BalanceStatus[] statuses);

    Double amount(AssetType assetType, MarginType marginType, BalanceStatus status);

}
