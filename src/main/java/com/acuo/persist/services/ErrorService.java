package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.common.model.ids.TradeId;
import com.acuo.persist.entity.ServiceError;

import java.util.List;

public interface ErrorService extends Service<ServiceError, String> {

    public void persist(AssetId assetId, List<ServiceError> serviceError);

    public void persist(TradeId tradeId, ServiceError serviceError);
}
