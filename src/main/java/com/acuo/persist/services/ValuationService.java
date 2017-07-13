package com.acuo.persist.services;

import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.AssetValuation;
import com.acuo.persist.entity.MarginValuation;
import com.acuo.persist.entity.TradeValuation;
import com.acuo.persist.entity.Valuation;
import com.acuo.persist.ids.AssetId;
import com.acuo.persist.ids.PortfolioId;
import com.acuo.persist.ids.TradeId;

import java.time.LocalDate;

public interface ValuationService extends Service<Valuation, String> {

    TradeValuation getTradeValuationFor(TradeId tradeId);

    TradeValuation getOrCreateTradeValuationFor(TradeId tradeId);

    MarginValuation getMarginValuationFor(PortfolioId portfolioId, Types.CallType callType);

    MarginValuation getOrCreateMarginValuationFor(PortfolioId portfolioId, Types.CallType callType);

    AssetValuation getAssetValuationFor(AssetId assetId);

    AssetValuation getOrCreateAssetValuationFor(AssetId assetId);

    Long tradeCount(PortfolioId portfolioId);

    Long tradeValuedCount(PortfolioId portfolioId, LocalDate valuationDate);
}
