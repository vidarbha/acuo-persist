package com.acuo.persist.services;

import com.acuo.persist.entity.Valuation;
import com.acuo.persist.ids.PortfolioId;

public interface ValuationService extends Service<Valuation>{

    Iterable<Valuation> allTradeValuationFor(PortfolioId portfolioId);

    Iterable<Valuation> allMarginValuationFor(PortfolioId portfolioId);
}
