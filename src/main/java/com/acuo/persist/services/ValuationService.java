package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioId;
import com.acuo.common.ids.TradeId;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.MarginValuation;
import com.acuo.persist.entity.TradeValuation;
import com.acuo.persist.entity.Valuation;

import java.time.LocalDate;

public interface ValuationService extends Service<Valuation, String> {

    TradeValuation getTradeValuationFor(ClientId clientId, TradeId tradeId);

    TradeValuation getOrCreateTradeValuationFor(ClientId clientId, TradeId tradeId);

    MarginValuation getMarginValuationFor(ClientId clientId, PortfolioId portfolioId, Types.CallType callType);

    MarginValuation getOrCreateMarginValuationFor(ClientId clientId, PortfolioId portfolioId, Types.CallType callType);

    Long tradeCount(ClientId clientId, PortfolioId portfolioId);

    Long tradeValuedCount(ClientId clientId, PortfolioId portfolioId, LocalDate valuationDate);

    Long tradeNotValuedCount(ClientId clientId, PortfolioId portfolioId, LocalDate valuationDate);

    boolean isTradeValuated(ClientId clientId, TradeId tradeId, LocalDate valuationDate);

    boolean isPortfolioValuated(ClientId clientId, PortfolioId portfolioId, Types.CallType callType, LocalDate valuationDate);
}
