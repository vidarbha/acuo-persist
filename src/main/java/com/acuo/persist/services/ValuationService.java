package com.acuo.persist.services;

import com.acuo.common.ids.PortfolioId;
import com.acuo.common.ids.TradeId;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.MarginValuation;
import com.acuo.persist.entity.TradeValuation;
import com.acuo.persist.entity.Valuation;

import java.time.LocalDate;

public interface ValuationService extends Service<Valuation, String> {

    TradeValuation getTradeValuationFor(TradeId tradeId);

    TradeValuation getOrCreateTradeValuationFor(TradeId tradeId);

    MarginValuation getMarginValuationFor(PortfolioId portfolioId, Types.CallType callType);

    MarginValuation getOrCreateMarginValuationFor(PortfolioId portfolioId, Types.CallType callType);

    Long tradeCount(PortfolioId portfolioId);

    Long tradeValuedCount(PortfolioId portfolioId, LocalDate valuationDate);

    boolean isTradeValuated(TradeId tradeId, LocalDate valuationDate);

    boolean isPortfolioValuated(PortfolioId portfolioId, Types.CallType callType, LocalDate valuationDate);
}
