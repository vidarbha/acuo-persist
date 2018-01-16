package com.acuo.persist.services;

import com.acuo.common.ids.MarginStatementId;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.AssetTransfer;
import com.acuo.persist.entity.Collateral;
import com.acuo.persist.entity.CollateralValue;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.MarginStatement;
import com.acuo.persist.entity.enums.AssetTransferStatus;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

import static com.google.common.collect.ImmutableMap.of;

public class CollateralServiceImpl extends AbstractService<Collateral, Long> implements CollateralService {

    private final MarginStatementService marginStatementService;
    private final CollateralValueService collateralValueService;

    @Inject
    public CollateralServiceImpl(Provider<Session> session,
                                 MarginStatementService marginStatementService,
                                 CollateralValueService collateralValueService) {
        super(session);
        this.marginStatementService = marginStatementService;
        this.collateralValueService = collateralValueService;
    }

    @Override
    public Class<Collateral> getEntityType() {
        return Collateral.class;
    }

    @Override
    @Transactional
    public Collateral getCollateralFor(MarginStatementId statementId,
                                       Types.MarginType marginType,
                                       Types.AssetType assetType,
                                       Types.BalanceStatus status) {
        String query =
                "MATCH p=(value)<-[*0..1]-(collateral:Collateral {marginType: {marginType}, assetType: {assetType}, status: {status}})" +
                "-[:BALANCE]->(statement:MarginStatement {id:{id}}) " +
                "RETURN p, nodes(p), relationships(p)";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", statementId.toString(),
                "marginType", marginType.name(),
                "assetType", assetType.name(),
                "status", status.name());
        return dao.getSession().queryForObject(Collateral.class, query, parameters);
    }

    @Override
    @Transactional
    public Collateral getOrCreateCollateralFor(MarginStatementId statementId,
                                               Types.MarginType marginType,
                                               Types.AssetType assetType,
                                               Types.BalanceStatus status) {
        Collateral collateral = getCollateralFor(statementId, marginType, assetType, status);
        if (collateral == null) {
            collateral = new Collateral();
            collateral.setMarginType(marginType);
            collateral.setAssetType(assetType);
            collateral.setStatus(status);
            collateral.setStatement(marginStatementService.find(statementId.toString()));
            collateral = createOrUpdate(collateral);
        }
        return collateral;
    }

    @Override
    @Transactional
    public Optional<Collateral> handle(AssetTransfer transfer) {
        Handler handler = new Handler(transfer);
        return handler.handle();
    }

    @Override
    public Double amount(Types.AssetType assetType, Types.MarginType marginType, Types.BalanceStatus status) {
        String query =
                "MATCH (c:Collateral)-[:LATEST]->(d:CollateralValue) " +
                "WHERE c.marginType = {marginType} " +
                "AND c.assetType = {assetType} " +
                "AND c.status= {status} " +
                "RETURN sum(d.amount)";
        final ImmutableMap<String, String> parameters =
                of("assetType", assetType.name(),
                   "marginType", marginType.name(),
                   "status", status.name());
        return dao.getSession().queryForObject(Double.class, query, parameters);
    }

    private class Handler {

        private final Types.MarginType marginType;
        private final String statementId;
        private final Types.AssetType assetType;
        private final AssetTransfer transfer;

        Handler(AssetTransfer transfer) {
            this.transfer = transfer;
            MarginCall marginCall = transfer.getGeneratedBy();
            this.marginType = marginCall.getMarginType();

            MarginStatement marginStatement = marginCall.getMarginStatement();
            this.statementId = marginStatement.getStatementId();

            Asset asset = transfer.getOf();
            String type = asset.getType();
            this.assetType = "CASH".equals(type) ? Types.AssetType.Cash : Types.AssetType.NonCash;
        }

        Optional<Collateral> handle() {
            if (transfer.getStatus().equals(AssetTransferStatus.Departed)) {
                return Optional.ofNullable(departed());
            }
            if (transfer.getStatus().equals(AssetTransferStatus.Deployed)) {
                return Optional.ofNullable(deployed());
            }
            return Optional.empty();
        }

        private Collateral departed() {
            Types.BalanceStatus status = AssetTransfer.status(transfer.getStatus());
            double amount = transfer.getQuantity() * transfer.getUnitValue();
            Collateral collateral = getOrCreateCollateralFor(MarginStatementId.fromString(statementId), marginType, assetType, status);
            CollateralValue value = collateralValueService.createValue(amount);
            value.setCollateral(collateral);
            collateral.setLatestValue(value);
            save(collateral, 2);
            return find(collateral.getId());
        }

        private Collateral deployed() {
            Collateral collateral = getCollateralFor(MarginStatementId.fromString(statementId), marginType, assetType, Types.BalanceStatus.Pending);
            if (collateral == null) return null;
            collateral.setStatus(Types.BalanceStatus.Settled);
            save(collateral, 2);
            return find(collateral.getId());
        }

    }

}
