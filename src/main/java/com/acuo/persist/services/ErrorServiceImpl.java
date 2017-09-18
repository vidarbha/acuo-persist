package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.common.model.ids.PortfolioId;
import com.acuo.common.model.ids.TradeId;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.Portfolio;
import com.acuo.persist.entity.ServiceError;
import com.acuo.persist.entity.Trade;
import com.google.inject.Inject;

import java.util.List;

public class ErrorServiceImpl extends GenericService<ServiceError, String> implements ErrorService {

    @Inject
    private TradeService<Trade> tradeService;

    @Inject
    private AssetService assetService;

    @Inject
    private PortfolioService portfolioService;

    @Override
    public Class<ServiceError> getEntityType() {
        return ServiceError.class;
    }

    @Override
    public void persist(AssetId assetId, List<ServiceError> serviceError) {
        if (assetId == null || serviceError == null) return;

        Asset asset = assetService.find(assetId);
        if (asset == null) return;
        asset.addAllErrors(serviceError);
        assetService.save(asset);
    }

    @Override
    public void persist(TradeId tradeId, ServiceError serviceError) {
        if (tradeId == null || serviceError == null) return;

        Trade trade = tradeService.find(tradeId);
        if (trade == null) return;
        trade.addErrors(serviceError);
        tradeService.save(trade);
    }

    @Override
    public void persist(PortfolioId portfolioId, ServiceError serviceError) {
        if (portfolioId == null || serviceError == null) return;

        Portfolio portfolio = portfolioService.find(portfolioId);
        if (portfolio == null) return;
        portfolio.addErrors(serviceError);
        portfolioService.save(portfolio);
    }

}
