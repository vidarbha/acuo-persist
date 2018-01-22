package com.acuo.persist.services;

import com.acuo.persist.entity.IRS;
import com.acuo.persist.entity.Trade;
import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioId;
import com.acuo.common.ids.TradeId;

import java.util.List;

public interface TradeService<T extends Trade> extends Service<T, TradeId> {

    Iterable<T> findBilateralTradesByClientId(ClientId clientId);

    <T extends Trade> Iterable<T> findByPortfolioId(ClientId clientId, PortfolioId... ids);

    Iterable<IRS> findAllIRS();

    <S extends T> Iterable<S> createOrUpdate(Iterable<S> trades);

    Iterable<T> findAllTradeByIds(List<TradeId> ids);
}