package com.acuo.persist.services;

import com.acuo.common.model.ids.MarginStatementId;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.AssetTransfer;
import com.acuo.persist.entity.Collateral;

public interface CollateralService extends Service<Collateral, Long> {

    Collateral getCollateralFor(MarginStatementId statementId,
                                Types.MarginType marginType,
                                Types.AssetType assetType,
                                Types.BalanceStatus status);

    Collateral getOrCreateCollateralFor(MarginStatementId statementId,
                                        Types.MarginType marginType,
                                        Types.AssetType assetType,
                                        Types.BalanceStatus status);

    Collateral handle(AssetTransfer transfer);

    Double amount(Types.AssetType assetType, Types.MarginType marginType, Types.BalanceStatus status);
}
