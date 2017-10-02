package com.acuo.persist.validations;

import com.acuo.persist.services.AssetPledgeService;
import com.acuo.persist.services.CollateralService;
import com.opengamma.strata.collect.result.FailureReason;
import com.opengamma.strata.collect.result.Result;

import javax.inject.Inject;

public class Checker {

    private final AssetPledgeService assetPledgeService;
    private final CollateralService collateralService;

    @Inject
    public Checker(AssetPledgeService assetPledgeService,
                   CollateralService collateralService) {
        this.assetPledgeService = assetPledgeService;
        this.collateralService = collateralService;
    }

    public Result<Double> isConsistent(CheckData data) {
        Double apv = assetPledgeService.amount(data.getAssetType(), data.getMarginType(), data.getBalanceStatus());
        Double cv = collateralService.amount(data.getAssetType(), data.getMarginType(), data.getBalanceStatus());
        if (Double.compare(apv, cv) == 0)
            return Result.success(apv);
        else {
            double diff = apv - cv;
            String message = "validations check failed for {} with a diff of {}";
            return Result.failure(FailureReason.OTHER, message, data, diff );
        }
    }
}
