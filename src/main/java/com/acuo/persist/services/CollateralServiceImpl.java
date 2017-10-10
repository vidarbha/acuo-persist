package com.acuo.persist.services;

import com.acuo.common.model.ids.MarginStatementId;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.AssetTransfer;
import com.acuo.persist.entity.Collateral;
import com.acuo.persist.entity.CollateralValue;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.MarginStatement;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

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
    public Collateral handle(AssetTransfer transfer) {

        MarginCall marginCall = transfer.getGeneratedBy();
        Types.MarginType marginType = marginCall.getMarginType();

        MarginStatement marginStatement = marginCall.getMarginStatement();
        String statementId = marginStatement.getStatementId();

        Asset asset = transfer.getOf();
        String type = asset.getType();
        Types.AssetType assetType = "CASH".equals(type) ? Types.AssetType.Cash : Types.AssetType.NonCash;

        Types.BalanceStatus status = AssetTransfer.status(transfer.getStatus());

        double amount = transfer.getQuantity() * transfer.getUnitValue();
        Collateral collateral = getOrCreateCollateralFor(MarginStatementId.fromString(statementId), marginType, assetType, status);
        CollateralValue value = collateralValueService.createValue(amount);
        value.setCollateral(collateral);
        collateral.setLatestValue(value);
        save(collateral, 2);
        return find(collateral.getId());
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

}
