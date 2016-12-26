package com.acuo.persist.services;

import com.acuo.persist.entity.Trade;
import com.google.inject.persist.Transactional;

public interface TradeService extends Service<Trade> {
    Iterable<Trade> findByClientId(String id);

    Iterable<Trade> findBilateralTradesByClientId(String clientId);
}
