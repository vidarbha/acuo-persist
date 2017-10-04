package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.persist.entity.Settlement;
import com.google.common.collect.ImmutableMap;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class SettlementServiceImpl extends AbstractService<Settlement, String> implements SettlementService{

    private final AssetService assetService;

    @Inject
    public SettlementServiceImpl(Provider<Session> session, AssetService assetService) {
        super(session);
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
        return dao.getSession().queryForObject(Settlement.class, query, parameters);
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