package com.acuo.persist.services;

import com.acuo.persist.entity.Trade;
import com.acuo.persist.ids.ClientId;
import com.acuo.persist.ids.PortfolioId;
import com.google.inject.persist.Transactional;

import java.util.Iterator;

public interface TradeService<T extends Trade> extends Service<T> {

    Iterable<T> findBilateralTradesByClientId(ClientId clientId);

    Iterable<T> findByPortfolioId(PortfolioId portfolioId);

    Iterable<T> findAllIRS();

    <S extends T> Iterable<S> createOrUpdate(Iterable<S> trades);
}
