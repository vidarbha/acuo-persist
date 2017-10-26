package com.acuo.persist.services;

import com.acuo.common.ids.TradeId;
import com.acuo.persist.entity.FxSwap;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class FxSwapServiceImpl extends AbstractService<FxSwap, TradeId> implements FxSwapService {

    @Inject
    public FxSwapServiceImpl(Provider<Session> session) {
        super(session);
    }

    public Class<FxSwap> getEntityType() {
        return FxSwap.class;
    }
}

