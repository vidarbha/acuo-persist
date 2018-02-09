package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioName;
import com.acuo.common.ids.TradeId;
import com.acuo.persist.entity.Agreement;

public interface AgreementService extends Service<Agreement, String> {

    Agreement agreementFor(ClientId clientId, PortfolioName portfolioName);

    Agreement agreementFor(ClientId clientId, TradeId tradeId);
}
