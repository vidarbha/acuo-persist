package com.acuo.persist.services;

import com.acuo.persist.entity.Portfolio;
import com.acuo.common.model.ids.PortfolioId;
import com.acuo.common.model.ids.TradeId;

public interface PortfolioService extends Service<Portfolio, PortfolioId> {

    Portfolio findBy(TradeId tradeId);

    Long tradeCount(PortfolioId portfolioId);

}
