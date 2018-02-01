package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.PortfolioId;
import com.acuo.common.ids.TradeId;
import com.acuo.persist.entity.Agreement;

public interface AgreementService extends Service<Agreement, String> {

    Agreement agreementFor(ClientId clientId, PortfolioId portfolioId);

    Agreement agreementFor(ClientId clientId, TradeId tradeId);
}
