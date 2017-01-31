package com.acuo.persist.services;

import com.acuo.persist.entity.Asset;
import com.acuo.persist.ids.ClientId;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

@Transactional
public class AssetServiceImpl extends GenericService<Asset> implements AssetService{

    private static String ELIGIBLE_ASSET_WITH_ACCT_AND_TRANSFER_INFO =
            "MATCH path=(client:Client {id:{clientId}})-[:MANAGES]->(entity:LegalEntity)-[:HAS]->(:TradingAccount)" +
            "-[:ACCESSES]->(ca:CustodianAccount)-[holds:HOLDS]->(a:Asset)-[:IS_AVAILABLE_FOR]->(agreement:Agreement) " +
            "WHERE (entity)-[:CLIENT_SIGNS]->(agreement) " +
            "WITH a, entity, path " +
            "OPTIONAL MATCH transfer=(a:Asset)<-[:OF]-(:AssetTransfer)-[:FROM|TO]->(:CustodianAccount)<-[:HAS]-(entity) " +
            "RETURN a, nodes(path), rels(path), nodes(transfer), rels(transfer)";

    private static String ELIGIBLE_ASSET_BY_CLIENT_AND_CALLID =
            "MATCH path=(client:Client {id:{clientId}})-[:MANAGES]->(entity:LegalEntity)-[:HAS]->(:TradingAccount)" +
            "-[:ACCESSES]->(ca:CustodianAccount)-[holds:HOLDS]-(asset:Asset)-[is:IS_AVAILABLE_FOR]->(agreement:Agreement)<-" +
            "[:STEMS_FROM]-(marginCall:MarginCall {id:{callId}})-[*1..2]->(ms:MarginStatement)" +
            "WHERE marginCall.marginType IN is.marginType " +
            "(ms)-[:DIRECTED_TO]->(entity)" +
            "AND (entity)-[:CLIENT_SIGNS]-(agreement) " +
            "AND NOT (asset)-[:EXCLUDED]->(marginCall) " +
            "RETURN a, nodes(path), rels(path)";

    private static String RESERVED_ASSET_BY_CLIENT_ID =
            "MATCH path=(client:Client {id:{clientId}})-[:MANAGES]->(entity:LegalEntity)-[:HAS]->(:TradingAccount)" +
            "-[:ACCESSES]->(ca:CustodianAccount)-[holds:HOLDS]->(asset:Asset)-[:IS_AVAILABLE_FOR]->(agreement:Agreement) " +
            "WHERE (entity)-[:CLIENT_SIGNS]->(agreement) " +
            "AND holds.reservedQuantity>0 " +
            "RETURN asset, nodes(path), rels(path)";

    @Override
    public Iterable<Asset> findEligibleAssetByClientId(ClientId clientId) {
        String query = ELIGIBLE_ASSET_WITH_ACCT_AND_TRANSFER_INFO;
        return session.query(getEntityType(), query, ImmutableMap.of("clientId",clientId.toString()));
    }

    @Override
    public Iterable<Asset> findReservedAssetByClientId(ClientId clientId) {
        String query = RESERVED_ASSET_BY_CLIENT_ID;
        return session.query(getEntityType(), query, ImmutableMap.of("clientId",clientId.toString()));
    }

    @Override
    public Iterable<Asset> findAvailableAssetByClientIdAndCallIds(ClientId clientId, String callId) {
        String query = ELIGIBLE_ASSET_BY_CLIENT_AND_CALLID;
        return session.query(getEntityType(), query, ImmutableMap.of("clientId",clientId.toString(), "callId", callId));
    }

    @Override
    public Class<Asset> getEntityType() {
        return Asset.class;
    }
}
