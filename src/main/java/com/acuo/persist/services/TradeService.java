package com.acuo.persist.services;

import com.acuo.persist.entity.Trade;
import com.google.inject.persist.Transactional;

import java.util.Iterator;

public interface TradeService<T extends Trade> extends Service<T> {

    Iterable<T> findBilateralTradesByClientId(String clientId);

    T findById(Long id);

    Iterable<T> findByPortfolioId(String portfolioId);

    Iterable<T> findAllIRS();

}
