package com.acuo.persist.services;

import com.acuo.persist.entity.Asset;
import com.acuo.common.model.ids.AssetId;
import com.acuo.common.model.ids.ClientId;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.model.Result;

import java.util.Map;

public class AssetServiceImpl extends GenericService<Asset, AssetId> implements AssetService {

    private final static String AVAILABLE_ASSET =
            "MATCH (client:Client {id:{clientId}})-[:MANAGES]->(entity:LegalEntity)-[:CLIENT_SIGNS]->(agreement:Agreement)-[:IS_COMPOSED_OF]->(rule:Rule)-[:APPLIES_TO]->(asset:Asset) " +
            "WITH asset, client, rule " +
            "MATCH h=(:Custodian)-[:MANAGES]->(ca:CustodianAccount)-[holds:HOLDS]->(asset) " +
            "MATCH v=(asset)<-[:VALUATED]-(:AssetValuation)-[:VALUE]->(:AssetValue) " +
            "RETURN asset, " +
            "nodes(h), relationships(h), " +
            "nodes(v), relationships(v)";

    private final static String ELIGIBLE_ASSET_BY_CLIENT_AND_CALLID =
            "MATCH (client:Client {id:{clientId}})-[:MANAGES]->(entity:LegalEntity)-[:CLIENT_SIGNS]->(agreement:Agreement)-[:IS_COMPOSED_OF]->(rule:Rule)-[:APPLIES_TO]->(asset:Asset) " +
            "WITH asset, client, agreement, entity, rule " +
            "MATCH (agreement)<-[:STEMS_FROM]-(ms:MarginStatement)<-[*1..2]-(marginCall:MarginCall {id:{callId}}),(ms)-[:SENT_FROM|DIRECTED_TO]->(entity) " +
            "WHERE marginCall.marginType IN rule.marginType " +
            "AND NOT (asset)-[:EXCLUDED]->(marginCall) " +
            "WITH DISTINCT asset, rule " +
            "MATCH h=(:Custodian)-[:MANAGES]->(ca:CustodianAccount)-[:HOLDS]->(asset) " +
            "MATCH v=(asset)<-[:VALUATED]-(:AssetValuation)-[:VALUE]->(:AssetValue) " +
            "MATCH r=(rule)-[:APPLIES_TO]->(asset) " +
            "RETURN asset, " +
            "nodes(h), relationships(h), " +
            "nodes(v), relationships(v), " +
            "nodes(r), relationships(r)";

    private final static String TOTAL_HAIRCUT =
            "MATCH (si:StatementItem {id:{callId}})-[:PART_OF]->(:MarginStatement)-[:STEMS_FROM]->(agr:Agreement) " +
            "MATCH (a:Asset {id:{assetId}})-[:IS_IN]->(:AssetCategory)-[eu:IS_ELIGIBLE_UNDER]->(agr) " +
            "WITH eu.haircut + eu.FXHaircut as totalHaircut " +
            "RETURN totalHaircut";

    @Override
    @Transactional
    public Iterable<Asset> findAvailableAssetByClientId(ClientId clientId) {
        final ImmutableMap<String, String> parameters = ImmutableMap.of("clientId", clientId.toString());
        return sessionProvider.get().query(getEntityType(), AVAILABLE_ASSET, parameters);
    }

    @Override
    @Transactional
    public Iterable<Asset> findAvailableAssetByClientIdAndCallId(ClientId clientId, String callId) {
        final ImmutableMap<String, String> parameters = ImmutableMap.of("clientId", clientId.toString(),
                "callId", callId);
        return sessionProvider.get().query(getEntityType(), ELIGIBLE_ASSET_BY_CLIENT_AND_CALLID, parameters);
    }

    @Override
    @Transactional
    public Double totalHaircut(AssetId assetId, String callId) {
        final ImmutableMap<String, String> parameters = ImmutableMap.of("assetId", assetId.toString(),
                "callId", callId);
        Result result = sessionProvider.get().query(TOTAL_HAIRCUT, parameters);
        Map<String, Object> next = result.iterator().next();
        return (Double) next.get("totalHaircut");
    }

    @Override
    public Class<Asset> getEntityType() {
        return Asset.class;
    }
}
