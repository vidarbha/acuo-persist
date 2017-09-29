package com.acuo.persist.consistency;

import com.acuo.common.model.margin.Types;
import com.acuo.persist.services.AssetPledgeService;
import com.acuo.persist.services.CollateralService;
import com.google.common.collect.ImmutableList;
import com.opengamma.strata.collect.result.Result;
import lombok.Value;

import javax.inject.Inject;
import java.util.List;

import static com.acuo.common.model.margin.Types.BalanceStatus;
import static com.acuo.common.model.margin.Types.MarginType;

public class CheckManagerImpl implements CheckManager {

    @Override
    public List<Result> check() {
        return ImmutableList.of(Result.success(0.0d));
    }

    private static class Checker {

        private final AssetPledgeService assetPledgeService;
        private final CollateralService collateralService;

        @Inject
        public Checker(AssetPledgeService assetPledgeService,
                       CollateralService collateralService) {
            this.assetPledgeService = assetPledgeService;
            this.collateralService = collateralService;
        }

        public Result isConsistent(CheckData data) {
            Double amount = assetPledgeService.amount(MarginType.values(), BalanceStatus.values());
            return Result.success(0.0d);
        }
    }

    @Value
    private static class CheckData {
        private final MarginType marginType;
        private final BalanceStatus balanceStatus;
        private final Types.AssetType assetType;
    }
}