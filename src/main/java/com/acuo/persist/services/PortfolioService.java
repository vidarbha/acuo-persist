package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.persist.entity.Portfolio;
import com.acuo.common.ids.PortfolioId;
import com.acuo.common.ids.TradeId;

public interface PortfolioService extends Service<Portfolio, PortfolioId> {

    Portfolio findBy(TradeId tradeId);

    Iterable<Portfolio> portfolios(ClientId clientId, PortfolioId... ids);

    Portfolio portfolio(ClientId clientId, PortfolioId id);

    Long tradeCount(ClientId clientId, PortfolioId portfolioId);

}
