package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioName;
import com.acuo.common.ids.TradeId;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.MarginValuation;
import com.acuo.persist.entity.TradeValuation;
import com.acuo.persist.entity.Valuation;

import java.time.LocalDate;

public interface ValuationService extends Service<Valuation, String> {

    TradeValuation getTradeValuationFor(ClientId clientId, TradeId tradeId);

    TradeValuation getOrCreateTradeValuationFor(ClientId clientId, TradeId tradeId);

    MarginValuation getMarginValuationFor(ClientId clientId, PortfolioName portfolioName, Types.CallType callType);

    MarginValuation getOrCreateMarginValuationFor(ClientId clientId, PortfolioName portfolioName, Types.CallType callType);

    Long tradeCount(ClientId clientId, PortfolioName portfolioName);

    Long tradeValuedCount(ClientId clientId, PortfolioName portfolioName, LocalDate valuationDate);

    Long tradeNotValuedCount(ClientId clientId, PortfolioName portfolioName, LocalDate valuationDate);

    boolean isTradeValuated(ClientId clientId, TradeId tradeId, LocalDate valuationDate);

    boolean isPortfolioValuated(ClientId clientId, PortfolioName portfolioId, Types.CallType callType, LocalDate valuationDate);
}
