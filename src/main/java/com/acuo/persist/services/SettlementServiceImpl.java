package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.persist.entity.Settlement;
import com.acuo.persist.entity.SettlementDate;
import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;

public class SettlementServiceImpl extends GenericService<Settlement, String> implements SettlementService{

    private final AssetService assetService;

    @Inject
    public SettlementServiceImpl(AssetService assetService) {
        this.assetService = assetService;
    }

    @Override
    public Class<Settlement> getEntityType() {
        return Settlement.class;
    }

    @Override
    public Settlement getSettlementFor(AssetId assetId) {
        String query =
                "MATCH p=(child:SettlementDate)<-[:SETTLEMENT_DATE]-(sd:Settlement)<-[:SETTLEMENT]-(asset:Asset {id:{id}}) " +
                "RETURN sd, nodes(p), relationships(p)";
        ImmutableMap<String, String> parameters = ImmutableMap.of("id", assetId.toString());
        return sessionProvider.get().queryForObject(Settlement.class, query, parameters);
    }

    @Override
    public Settlement getOrCreateSettlementFor(AssetId assetId) {
        Settlement settlement = getSettlementFor(assetId);
        if (settlement == null) {
            settlement = new Settlement();
            settlement.setSettlementId(assetId.toString() + "-settlement");
            settlement.setAsset(assetService.find(assetId));
            save(settlement);
        }
        return settlement;
    }
}