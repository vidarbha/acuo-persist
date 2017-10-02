package com.acuo.persist.validations;

import com.google.common.collect.ImmutableList;
import com.opengamma.strata.collect.result.Result;

import javax.inject.Inject;
import java.util.List;

import static com.acuo.common.model.margin.Types.AssetType.Cash;
import static com.acuo.common.model.margin.Types.AssetType.NonCash;
import static com.acuo.common.model.margin.Types.BalanceStatus.Pending;
import static com.acuo.common.model.margin.Types.BalanceStatus.Settled;
import static com.acuo.common.model.margin.Types.MarginType.Initial;
import static com.acuo.common.model.margin.Types.MarginType.Variation;
import static java.util.stream.Collectors.toList;

public class CheckManagerImpl implements CheckManager {

    private final Checker checker;
    private static final List<CheckData> checks = ImmutableList.of(
            CheckData.of(Variation, Pending, Cash),
            CheckData.of(Variation, Pending, NonCash),
            CheckData.of(Variation, Settled, Cash),
            CheckData.of(Variation, Settled, NonCash),
            CheckData.of(Initial, Pending, Cash),
            CheckData.of(Initial, Pending, NonCash),
            CheckData.of(Initial, Settled, Cash),
            CheckData.of(Initial, Settled, NonCash)
    );

    @Inject
    public CheckManagerImpl(Checker checker) {
        this.checker = checker;
    }

    @Override
    public List<Result> check() {
        return checks.stream()
                .map(checker::isConsistent)
                .collect(toList());
    }
}