package com.acuo.persist.services;

import com.acuo.persist.entity.Asset;
import com.acuo.persist.ids.ClientId;
import com.google.common.collect.ImmutableMap;

public class AssetServiceImpl extends GenericService<Asset, String> implements AssetService {

    private static String ELIGIBLE_ASSET_WITH_ACCT_AND_TRANSFER_INFO =
            "MATCH (client:Client {id:{clientId}})-[:MANAGES]->(entity:LegalEntity)-[:CLIENT_SIGNS]->(agreement:Agreement)-[:IS_COMPOSED_OF]->(rule:Rule)-[:APPLIES_TO]->(asset:Asset) " +
            "WITH asset, client, rule " +
            "MATCH h=(:Custodian)-[:MANAGES]->(ca:CustodianAccount)-[holds:HOLDS]->(asset) " +
            "MATCH v=(asset)<-[:VALUATED]-(:AssetValuation)-[:VALUE]->(:AssetValue) " +
            "OPTIONAL MATCH t=(asset)<-[:OF]-(:AssetTransfer)-[:FROM|TO]->(:CustodianAccount)<-[:HAS]-(client) " +
            "RETURN asset, " +
            "nodes(h), relationships(h), " +
            "nodes(v), relationships(v), " +
            "nodes(t), relationships(t)";

    private static String ELIGIBLE_ASSET_BY_CLIENT_AND_CALLID =
            "MATCH (client:Client {id:{clientId}})-[:MANAGES]->(entity:LegalEntity)-[:CLIENT_SIGNS]->(agreement:Agreement)-[:IS_COMPOSED_OF]->(rule:Rule)-[:APPLIES_TO]->(asset:Asset) " +
            "WITH asset, client, agreement, entity, rule " +
            "MATCH (agreement)<-[:STEMS_FROM]-(ms:MarginStatement)<-[*1..2]-(marginCall:MarginCall {id:{callId}}),(ms)-[:DIRECTED_TO]->(entity) " +
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

    @Override
    public Iterable<Asset> findEligibleAssetByClientId(ClientId clientId) {
        String query = ELIGIBLE_ASSET_WITH_ACCT_AND_TRANSFER_INFO;
        return sessionProvider.get().query(getEntityType(), query, ImmutableMap.of("clientId",clientId.toString()));
    }

    @Override
    public Iterable<Asset> findAvailableAssetByClientIdAndCallId(ClientId clientId, String callId) {
        String query = ELIGIBLE_ASSET_BY_CLIENT_AND_CALLID;
        return sessionProvider.get().query(getEntityType(), query, ImmutableMap.of("clientId",clientId.toString(), "callId", callId));
    }

    @Override
    public Class<Asset> getEntityType() {
        return Asset.class;
    }
}
