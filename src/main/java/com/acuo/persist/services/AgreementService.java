package com.acuo.persist.services;

import com.acuo.persist.entity.Agreement;
import com.acuo.common.ids.PortfolioId;
import com.acuo.common.ids.TradeId;

public interface AgreementService extends Service<Agreement, String> {

    Agreement agreementFor(PortfolioId portfolioId);

    Agreement agreementFor(TradeId tradeId);
}
