package com.acuo.persist.services;

import com.acuo.persist.entity.Asset;
import com.acuo.persist.ids.ClientId;
import com.google.common.collect.ImmutableMap;

public class AssetServiceImpl extends GenericService<Asset> implements AssetService{

    private static String ELIGIBLE_ASSET_WITH_ACCT_AND_TRANSFER_INFO =
            "MATCH path=(client:Client {id:{clientId}})-[:MANAGES]->(entity:LegalEntity)-[:HAS]->(:TradingAccount)" +
            "-[:ACCESSES]->(ca:CustodianAccount)-[holds:HOLDS]->(a:Asset)-[:IS_AVAILABLE_FOR]->(agreement:Agreement) " +
            "WHERE (entity)-[:CLIENT_SIGNS]->(agreement) " +
            "WITH a, client, path " +
            "OPTIONAL MATCH transfer=(a:Asset)<-[:OF]-(:AssetTransfer)-[:FROM|TO]->(:CustodianAccount)<-[:HAS]-(client) " +
            "RETURN a, nodes(path), relationships(path), nodes(transfer), relationships(transfer)";

    private static String ELIGIBLE_ASSET_BY_CLIENT_AND_CALLID =
            "MATCH (client:Client {id:{clientId}})-[:MANAGES]->(entity:LegalEntity)-[:HAS]->(:TradingAccount)" +
            "-[:ACCESSES]->(ca:CustodianAccount)-[holds:HOLDS]->(asset:Asset)-[is:IS_AVAILABLE_FOR]->(agreement:Agreement)" +
            "<-[:STEMS_FROM]-(ms:MarginStatement)<-[*1..2]-(marginCall:MarginCall {id:{callId}}), " +
            "(entity)-[:CLIENT_SIGNS]-(agreement), " +
            "(ms)-[:DIRECTED_TO]->(entity) " +
            "WHERE marginCall.marginType IN is.marginType " +
            "AND NOT (asset)-[:EXCLUDED]->(marginCall) " +
            "WITH DISTINCT asset, ca, is " +
            "MATCH path=(Custodian)-[MANAGES]->(ca)-[holds]->(asset)-[is]-() " +
            "RETURN asset, nodes(path), relationships(path)";

    private static String RESERVED_ASSET_BY_CLIENT_ID =
            "MATCH path=(client:Client {id:{clientId}})-[:MANAGES]->(entity:LegalEntity)-[:HAS]->(:TradingAccount)" +
            "-[:ACCESSES]->(ca:CustodianAccount)-[holds:HOLDS]->(asset:Asset)-[:IS_AVAILABLE_FOR]->(agreement:Agreement) " +
            "WHERE (entity)-[:CLIENT_SIGNS]->(agreement) " +
            "AND holds.reservedQuantity>0 " +
            "RETURN asset, nodes(path), relationships(path)";

    @Override
    public Iterable<Asset> findEligibleAssetByClientId(ClientId clientId) {
        String query = ELIGIBLE_ASSET_WITH_ACCT_AND_TRANSFER_INFO;
        return sessionProvider.get().query(getEntityType(), query, ImmutableMap.of("clientId",clientId.toString()));
    }

    @Override
    public Iterable<Asset> findReservedAssetByClientId(ClientId clientId) {
        String query = RESERVED_ASSET_BY_CLIENT_ID;
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
