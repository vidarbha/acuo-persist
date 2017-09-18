package com.acuo.persist.services;

import com.acuo.common.model.ids.TradeId;
import com.acuo.persist.entity.FxSwap;

public class FxSwapServiceImpl extends GenericService<FxSwap, TradeId> implements FxSwapService {

    public Class<FxSwap> getEntityType() {
        return FxSwap.class;
    }
}

