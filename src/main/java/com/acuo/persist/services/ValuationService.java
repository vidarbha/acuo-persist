package com.acuo.persist.services;

import com.acuo.persist.entity.AssetValuation;
import com.acuo.persist.entity.MarginValuation;
import com.acuo.persist.entity.TradeValuation;
import com.acuo.persist.entity.Valuation;
import com.acuo.persist.ids.AssetId;
import com.acuo.persist.ids.PortfolioId;
import com.acuo.persist.ids.TradeId;

public interface ValuationService extends Service<Valuation, String> {

    TradeValuation getTradeValuationFor(TradeId tradeId);

    TradeValuation getOrCreateTradeValuationFor(TradeId tradeId);

    MarginValuation getMarginValuationFor(PortfolioId portfolioId);

    MarginValuation getOrCreateMarginValuationFor(PortfolioId portfolioId);

    AssetValuation getAssetValuationFor(AssetId assetId);

    AssetValuation getOrCreateAssetValuationFor(AssetId assetId);
}
