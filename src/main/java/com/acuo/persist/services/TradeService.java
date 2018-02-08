package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioId;
import com.acuo.common.ids.TradeId;
import com.acuo.persist.entity.trades.Trade;

import java.util.List;

public interface TradeService<T extends Trade> extends Service<T, TradeId> {

    T findTradeBy(ClientId clientId, TradeId tradeId);

    Iterable<T> findAllClientTrades(ClientId clientId);

    Iterable<T> findByPortfolioId(ClientId clientId, PortfolioId... ids);

    Iterable<T> findAllTradeByIds(ClientId clientId, List<TradeId> ids);

    <S extends T> Iterable<S> createOrUpdate(ClientId clientId, Iterable<S> trades);
}