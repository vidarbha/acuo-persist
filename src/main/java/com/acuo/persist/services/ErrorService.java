package com.acuo.persist.services;

import com.acuo.common.ids.AssetId;
import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.MarginStatementId;
import com.acuo.common.ids.PortfolioName;
import com.acuo.common.ids.TradeId;
import com.acuo.persist.entity.AlgoError;
import com.acuo.persist.entity.ServiceError;

import java.util.List;

public interface ErrorService extends Service<ServiceError, String> {

    void persist(AssetId assetId, List<ServiceError> errors);

    void persist(ClientId clientId, TradeId tradeId, ServiceError error);

    void persist(ClientId clientId, PortfolioName portfolioName, ServiceError error);

    void persist(MarginStatementId statementId, AlgoError error);
}
