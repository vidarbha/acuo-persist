package com.acuo.persist.services;

import com.acuo.common.ids.AssetId;
import com.acuo.common.ids.PortfolioId;
import com.acuo.common.ids.TradeId;
import com.acuo.persist.entity.ServiceError;

import java.util.List;

public interface ErrorService extends Service<ServiceError, String> {

    public void persist(AssetId assetId, List<ServiceError> serviceError);

    public void persist(TradeId tradeId, ServiceError serviceError);

    public void persist(PortfolioId portfolioId, ServiceError serviceError);
}
