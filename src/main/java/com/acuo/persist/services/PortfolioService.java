package com.acuo.persist.services;

import com.acuo.persist.entity.Portfolio;
import com.acuo.persist.ids.TradeId;

public interface PortfolioService extends Service<Portfolio>{

    Portfolio findBy(TradeId tradeId);

}
