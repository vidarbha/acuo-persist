package com.acuo.persist.consistency;

import com.acuo.common.model.ids.AssetId;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.AssetTransfer;
import com.acuo.persist.services.AssetPledgeService;
import com.acuo.persist.services.CollateralService;
import com.opengamma.strata.collect.result.Result;
import javafx.print.Printer;

import javax.inject.Inject;

import static com.acuo.common.model.margin.Types.*;

public class BalanceCheckerImpl implements BalanceChecker {

    private final AssetPledgeService assetPledgeService;
    private final CollateralService collateralService;

    @Inject
    public BalanceCheckerImpl(AssetPledgeService assetPledgeService,
                              CollateralService collateralService) {
        this.assetPledgeService = assetPledgeService;
        this.collateralService = collateralService;
    }

    @Override
    public Result check(AssetTransfer transfer) {

        Double amount = assetPledgeService.amount(MarginType.values(), BalanceStatus.values());
        
        return Result.success(0.0d);
    }
}