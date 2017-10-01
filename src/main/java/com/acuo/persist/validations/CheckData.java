package com.acuo.persist.validations;

import com.acuo.common.model.margin.Types;
import lombok.Value;

@Value(staticConstructor = "of")
class CheckData {
    private final Types.MarginType marginType;
    private final Types.BalanceStatus balanceStatus;
    private final Types.AssetType assetType;
}
