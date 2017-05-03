package com.acuo.persist.services;

import com.acuo.persist.entity.IRS;
import com.acuo.persist.entity.Trade;
import com.acuo.persist.ids.ClientId;
import com.acuo.persist.ids.PortfolioId;

public interface TradeService<T extends Trade> extends Service<T, String> {

    Iterable<T> findBilateralTradesByClientId(ClientId clientId);

    Iterable<T> findByPortfolioId(PortfolioId portfolioId);

    Iterable<IRS> findAllIRS();

    <S extends T> Iterable<S> createOrUpdate(Iterable<S> trades);
}
