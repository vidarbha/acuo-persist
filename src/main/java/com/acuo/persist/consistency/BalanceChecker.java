package com.acuo.persist.consistency;

import com.acuo.persist.entity.AssetTransfer;
import com.opengamma.strata.collect.result.Result;

public interface BalanceChecker {

    Result check(AssetTransfer transfer);
}
