package com.acuo.persist.services;

import com.acuo.common.ids.AssetId;
import com.acuo.common.ids.ClientId;
import com.acuo.common.ids.MarginStatementId;
import com.acuo.common.ids.PortfolioId;
import com.acuo.common.ids.TradeId;
import com.acuo.persist.entity.AlgoError;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.MarginStatement;
import com.acuo.persist.entity.Portfolio;
import com.acuo.persist.entity.ServiceError;
import com.acuo.persist.entity.trades.Trade;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class ErrorServiceImpl extends AbstractService<ServiceError, String> implements ErrorService {

    private final TradeService<Trade> tradeService;
    private final AssetService assetService;
    private final PortfolioService portfolioService;
    private final MarginStatementService statementService;

    @Inject
    public ErrorServiceImpl(Provider<Session> session,
                            TradeService<Trade> tradeService,
                            AssetService assetService,
                            PortfolioService portfolioService,
                            MarginStatementService statementService) {
        super(session);
        this.tradeService = tradeService;
        this.assetService = assetService;
        this.portfolioService = portfolioService;
        this.statementService = statementService;
    }

    @Override
    public Class<ServiceError> getEntityType() {
        return ServiceError.class;
    }

    @Override
    public void persist(AssetId assetId, List<ServiceError> errors) {
        if (assetId == null || errors == null) return;

        Asset asset = assetService.find(assetId);
        if (asset == null) return;
        asset.addAllErrors(errors);
        assetService.save(asset);
    }

    @Override
    public void persist(ClientId clientId, TradeId tradeId, ServiceError error) {
        if (tradeId == null || error == null) return;

        Trade trade = tradeService.findTradeBy(clientId, tradeId);
        if (trade == null) return;
        trade.addErrors(error);
        tradeService.save(trade);
    }

    @Override
    public void persist(ClientId clientId, PortfolioId portfolioId, ServiceError error) {
        if (portfolioId == null || error == null) return;

        Portfolio portfolio = portfolioService.portfolio(clientId, portfolioId);
        if (portfolio == null) return;
        portfolio.addErrors(error);
        portfolioService.save(portfolio);
    }

    @Override
    public void persist(MarginStatementId statementId, AlgoError error) {
        if (statementId == null || error == null) return;

        MarginStatement marginStatement = statementService.find(statementId.toString());
        if (marginStatement == null) return;
        error.setStatement(marginStatement);
        save(error);
    }
}
