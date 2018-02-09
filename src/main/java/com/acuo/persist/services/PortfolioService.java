package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioName;
import com.acuo.common.ids.TradeId;
import com.acuo.persist.entity.Portfolio;

public interface PortfolioService extends Service<Portfolio, Long> {

    Portfolio findBy(TradeId tradeId);

    Iterable<Portfolio> portfolios(ClientId clientId, PortfolioName... portfolioNames);

    Portfolio portfolio(ClientId clientId, PortfolioName portfolioName);

    Long tradeCount(ClientId clientId, PortfolioName portfolioName);

}
