package com.acuo.persist.services;

import com.acuo.persist.entity.MarginValuation;
import com.acuo.persist.entity.TradeValuation;
import com.acuo.persist.entity.Valuation;
import com.acuo.persist.ids.PortfolioId;

public interface ValuationService extends Service<Valuation>{

    TradeValuation getOrCreateTradeValuationFor(PortfolioId portfolioId);

    MarginValuation getOrCreateMarginValuationFor(PortfolioId portfolioId);
}
