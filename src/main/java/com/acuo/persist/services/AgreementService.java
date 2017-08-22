package com.acuo.persist.services;

import com.acuo.persist.entity.Agreement;
import com.acuo.common.model.ids.PortfolioId;
import com.acuo.common.model.ids.TradeId;

public interface AgreementService extends Service<Agreement, String> {

    Agreement agreementFor(PortfolioId portfolioId);

    Agreement agreementFor(TradeId tradeId);
}
