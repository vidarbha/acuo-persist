package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.AssetPledge;
import com.acuo.persist.entity.AssetPledgeValue;
import com.acuo.persist.entity.AssetTransfer;
import com.acuo.persist.entity.MarginCall;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

import javax.inject.Inject;

import static com.acuo.common.model.margin.Types.BalanceStatus;
import static com.acuo.common.model.margin.Types.MarginType;
import static com.google.common.collect.ImmutableMap.of;

public class AssetPledgeServiceImpl extends GenericService<AssetPledge, Long> implements AssetPledgeService {

    private final AssetService assetService;
    private final AssetPledgeValueService assetPledgeValueService;

    @Inject
    public AssetPledgeServiceImpl(AssetService assetService,
                                  AssetPledgeValueService assetPledgeValueService) {
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
        return sessionProvider.get().queryForObject(AssetPledge.class, query, parameters);
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
    public AssetPledge handle(AssetTransfer transfer) {
        MarginCall marginCall = transfer.getGeneratedBy();
        MarginType marginType = marginCall.getMarginType();

        Asset asset = transfer.getOf();

        BalanceStatus status = AssetTransfer.status(transfer.getStatus());

        double amount = transfer.getQuantities() * transfer.getTransferValue();//transfer.getUnitValue();
        AssetPledge assetPledge = getOrCreateFor(asset.getAssetId(), marginType, status);
        AssetPledgeValue value = assetPledgeValueService.createValue(amount);
        value.setAssetPledge(assetPledge);
        assetPledge.setLatestValue(value);
        assetPledge = save(assetPledge, 2);
        return find(assetPledge.getId());
    }

    @Override
    public Double amount(MarginType[] types, BalanceStatus[] statuses) {
        String query =
                "MATCH (value:AssetPledgeValue)<-[:VALUE]-(assetPledge:AssetPledge)-[:OF]->(:Asset) " +
                "WHERE assetPledge.status IN {statuses} " +
                "AND assetPledge.marginType IN {types} " +
                "RETURN sum(value.amount)";
        final ImmutableMap<String, Object[]> parameters = of("types", types,"statuses", statuses);
        return sessionProvider.get().queryForObject(Double.class, query, parameters);
    }
}
