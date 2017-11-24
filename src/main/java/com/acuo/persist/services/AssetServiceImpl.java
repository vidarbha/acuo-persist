package com.acuo.persist.services;

import com.acuo.common.ids.AssetId;
import com.acuo.common.ids.ClientId;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.SettlementDate;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;
import java.util.Optional;

public class AssetServiceImpl extends AbstractService<Asset, AssetId> implements AssetService {

    private final static String AVAILABLE_ASSET =
            "MATCH (client:Client)-[:MANAGES]->(entity)-[:CLIENT_SIGNS]->(agreement)-[:IS_COMPOSED_OF]->(rule)-[:APPLIES_TO]->(asset) " +
                    "WHERE client.id = {clientId} " +
                    "WITH asset, client " +
                    "MATCH h=()-[:MANAGES]->(ca)-[holds:HOLDS]->(asset) " +
            "MATCH s=(asset)-[:SETTLEMENT*0..1]->(settlement)-[:LATEST*0..1]->(settlementDate) " +
                    "RETURN h, s";

    private final static String ELIGIBLE_ASSET_BY_CLIENT_AND_CALLID =
            "MATCH (client:Client)-[:MANAGES]->(entity)-[:CLIENT_SIGNS]->(agreement)-[:IS_COMPOSED_OF]->(rule)-[:APPLIES_TO]->(asset) " +
                    "WHERE client.id = {clientId} " +
            "WITH asset, agreement, entity, rule " +
                    "MATCH (agreement)<-[:STEMS_FROM]-(ms)<-[*1..2]-(marginCall:MarginCall),(ms)-[:SENT_FROM|DIRECTED_TO]->(entity) " +
                    "WHERE marginCall.id = {callId} AND marginCall.marginType IN rule.marginType " +
            "AND NOT (asset)-[:EXCLUDED]->(marginCall) " +
            "WITH DISTINCT asset, rule " +
                    "MATCH h=()-[:MANAGES]->(ca)-[:HOLDS]->(asset) " +
            "MATCH s=(asset)-[:SETTLEMENT*0..1]->(settlement)-[:LATEST*0..1]->(settlementDate) " +
            "MATCH r=(rule)-[:APPLIES_TO]->(asset) " +
                    "RETURN h, s, r";

    private final static String TOTAL_HAIRCUT =
            "MATCH (si:StatementItem)-[:PART_OF]->()-[:STEMS_FROM]->(agr) " +
                    "WHERE si.id = {callId} " +
                    "MATCH (asset:Asset {id:{assetId}})-[:IS_IN]->()-[eu:IS_ELIGIBLE_UNDER]->(agr) " +
                    "WHERE asset.id = {assetId} " +
            "WITH eu.haircut + eu.FXHaircut as totalHaircut " +
            "RETURN totalHaircut";

    private final static String ASSET_INVENTORY =
            "MATCH (client:Client)-[:HAS]->(ca)-[:HOLDS]->(asset) " +
                    "WHERE client.id = {clientId}";

    @Inject
    public AssetServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    @Transactional
    public Iterable<Asset> findAssets(ClientId clientId) {
        final ImmutableMap<String, String> parameters = ImmutableMap.of("clientId", clientId.toString());
        return dao.getSession().query(getEntityType(), ASSET_INVENTORY, parameters);
    }

    @Override
    @Transactional
    public Iterable<Asset> findAvailableAssetByClientId(ClientId clientId) {
        final ImmutableMap<String, String> parameters = ImmutableMap.of("clientId", clientId.toString());
        return dao.getSession().query(getEntityType(), AVAILABLE_ASSET, parameters);
    }

    @Override
    @Transactional
    public Iterable<Asset> findAvailableAssetByClientIdAndCallId(ClientId clientId, String callId) {
        final ImmutableMap<String, String> parameters = ImmutableMap.of("clientId", clientId.toString(),
                "callId", callId);
        return dao.getSession().query(getEntityType(), ELIGIBLE_ASSET_BY_CLIENT_AND_CALLID, parameters);
    }

    @Override
    @Transactional
    public Double totalHaircut(AssetId assetId, String callId) {
        final ImmutableMap<String, String> parameters = ImmutableMap.of("assetId", assetId.toString(),
                "callId", callId);
        Result result = dao.getSession().query(TOTAL_HAIRCUT, parameters);
        Map<String, Object> next = result.iterator().next();
        return (Double) next.get("totalHaircut");
    }

    @Override
    public Optional<SettlementDate> settlementDate(AssetId assetId) {
        String query =  "MATCH (asset:Asset {id:{assetId}})-[:SETTLEMENT]->(:Settlement)-[:LATEST]->" +
                "(date:SettlementDate) RETURN date";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("assetId", assetId.toString());
        final SettlementDate settlementDate = dao.getSession().queryForObject(SettlementDate.class, query, parameters);
        return Optional.ofNullable(settlementDate);
    }

    @Override
    public Class<Asset> getEntityType() {
        return Asset.class;
    }
}
