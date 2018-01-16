package com.acuo.persist.services;

import com.acuo.common.ids.AssetId;
import com.acuo.common.model.margin.Types;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.AssetPledge;
import com.acuo.persist.entity.AssetPledgeValue;
import com.acuo.persist.entity.AssetTransfer;
import com.acuo.persist.entity.MarginCall;
import com.acuo.persist.entity.enums.AssetTransferStatus;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

import static com.acuo.common.model.margin.Types.*;
import static com.google.common.collect.ImmutableMap.of;

public class AssetPledgeServiceImpl extends AbstractService<AssetPledge, Long> implements AssetPledgeService {

    private final AssetService assetService;
    private final AssetPledgeValueService assetPledgeValueService;

    @Inject
    public AssetPledgeServiceImpl(Provider<Session> session,
                                  AssetService assetService,
                                  AssetPledgeValueService assetPledgeValueService) {
        super(session);
        this.assetService = assetService;
        this.assetPledgeValueService = assetPledgeValueService;
    }

    @Override
    public Class<AssetPledge> getEntityType() {
        return AssetPledge.class;
    }

    @Override
    @Transactional
    public AssetPledge getFor(AssetId assetId,
                              MarginType marginType,
                              BalanceStatus status) {
        String query =
                "MATCH p=(value)<-[*0..1]-(assetPledge:AssetPledge {marginType: {marginType}, status: {status}})" +
                "-[:OF]->(asset:Asset {id:{id}}) " +
                "RETURN p, nodes(p), relationships(p)";
        final ImmutableMap<String, String> parameters = of("id", assetId.toString(),
                "marginType", marginType.name(),
                "status", status.name());
        return dao.getSession().queryForObject(AssetPledge.class, query, parameters);
    }

    @Override
    @Transactional
    public AssetPledge getOrCreateFor(AssetId assetId,
                                      MarginType marginType,
                                      BalanceStatus status) {
        AssetPledge assetPledge = getFor(assetId, marginType, status);
        if (assetPledge == null) {
            assetPledge = new AssetPledge();
            assetPledge.setMarginType(marginType);
            assetPledge.setStatus(status);
            assetPledge.setAsset(assetService.find(assetId));
            assetPledge = createOrUpdate(assetPledge);
        }
        return assetPledge;
    }

    @Override
    @Transactional
    public Optional<AssetPledge> handle(AssetTransfer transfer) {
        Handler handler = new Handler(transfer);
        return handler.handle();
    }

    @Override
    public Double sum(MarginType[] types, BalanceStatus[] statuses) {
        String query =
                "MATCH (value:AssetPledgeValue)<-[:VALUE]-(assetPledge:AssetPledge)-[:OF]->(:Asset) " +
                "WHERE assetPledge.status IN {statuses} " +
                "AND assetPledge.marginType IN {types} " +
                "RETURN sum(value.amount)";
        final ImmutableMap<String, Object[]> parameters = of("types", types,"statuses", statuses);
        return dao.getSession().queryForObject(Double.class, query, parameters);
    }

    @Override
    public Double amount(AssetType assetType, MarginType marginType, BalanceStatus status) {
        String query =
                "MATCH (a:Asset)<-[:OF]-(ap:AssetPledge)-[:LATEST]->(apv:AssetPledgeValue) " +
                "WHERE a.type IN {assetTypes} " +
                "AND ap.marginType = {marginType} " +
                "AND ap.status = {status}" +
                "RETURN sum(apv.amount)";
        final ImmutableMap<String, Object> parameters =
                of("assetTypes", AssetSubType.of(assetType),
                "marginType", marginType.name(),
                "status", status.name());
        return dao.getSession().queryForObject(Double.class, query, parameters);
    }

    private class Handler {

        private final AssetTransfer transfer;
        private final MarginType marginType;
        private final Asset asset;
        private final BalanceStatus status;

        Handler(AssetTransfer transfer) {
            this.transfer = transfer;
            MarginCall marginCall = transfer.getGeneratedBy();
            this.marginType = marginCall.getMarginType();

            this.asset = transfer.getOf();
            this.status = AssetTransfer.status(transfer.getStatus());
        }

        Optional<AssetPledge> handle() {
            if (transfer.getStatus().equals(AssetTransferStatus.Departed)) {
                return Optional.ofNullable(departed());
            }
            if (transfer.getStatus().equals(AssetTransferStatus.Deployed)) {
                return Optional.ofNullable(deployed());
            }
            return Optional.empty();
        }

        private AssetPledge departed() {
            double amount = transfer.getQuantity() * transfer.getUnitValue();
            AssetPledge assetPledge = getOrCreateFor(asset.getAssetId(), marginType, status);
            AssetPledgeValue value = assetPledgeValueService.createValue(amount);
            value.setAssetPledge(assetPledge);
            assetPledge.setLatestValue(value);
            assetPledge = save(assetPledge, 2);
            return find(assetPledge.getId());
        }

        private AssetPledge deployed() {
            AssetPledge assetPledge = getFor(asset.getAssetId(), marginType, Types.BalanceStatus.Pending);
            if (assetPledge == null) return null;
            assetPledge.setStatus(BalanceStatus.Settled);
            assetPledge = save(assetPledge, 2);
            return find(assetPledge.getId());
        }

    }
}
