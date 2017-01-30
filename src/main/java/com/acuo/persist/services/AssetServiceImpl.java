package com.acuo.persist.services;

import com.acuo.persist.entity.Asset;
import com.acuo.persist.ids.ClientId;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

@Transactional
public class AssetServiceImpl extends GenericService<Asset> implements AssetService{

    private static String ELIGIBLE_ASSET_WITH_ACCT_INFO =
            "MATCH (entity:LegalEntity)<-[:MANAGES]-(client:Client {id:{clientId}})-[:CLIENT_ASSET]->(:ClientAsset)-[possesses:POSSESSES]->(asset:Asset)-[:IS_AVAILABLE_FOR]->(agreement:Agreement) " +
            "WHERE (entity)-[:CLIENT_SIGNS]->(agreement) " +
            "AND possesses.quantities>possesses.reservedQuantities " +
            "WITH asset " +
            "MATCH path=(asset)-[:IS_IN|HOLDS|POSSESSES]-() " +
            "RETURN asset, nodes(path), rels(path)";

    private static String ELIGIBLE_ASSET_WITH_ACCT_AND_TRANSFER_INFO =
            "MATCH (entity:LegalEntity)<-[:MANAGES]-(client:Client {id:'999'})-[:CLIENT_ASSET]->(:ClientAsset)-[possesses:POSSESSES]->" +
            "(a:Asset)-[:IS_AVAILABLE_FOR]->(agreement:Agreement) " +
            "WHERE (entity)-[:CLIENT_SIGNS]->(agreement) " +
            "WITH DISTINCT a " +
            "MATCH account=(cu:Custodian)-[:MANAGES]->(ca:CustodianAccount)-[:HOLDS]->(a)<-[p:POSSESSES]-() " +
            "WITH a, account " +
            "OPTIONAL MATCH transfer=(a:Asset)<-[:OF]-(:AssetTransfer) " +
            "RETURN a, nodes(account), rels(account), nodes(transfer), rels(transfer)";

    private static String ELIGIBLE_ASSET_BY_CLIENT_AND_CALLIDS =
            "MATCH assets=(cu:Custodian)-[:MANAGES]->(ca:CustodianAccount)-[h:HOLDS]-(a:Asset)-[is:IS_AVAILABLE_FOR]->(ag:Agreement)<-" +
            "[:STEMS_FROM]-(mc:MarginCall)-[*1..2]->(ms:MarginStatement)-[:DIRECTED_TO]->(e:LegalEntity)<-[:MANAGES]-(c:Client) " +
            "WHERE mc.id IN {callIds} " +
            "AND c.id = {clientId} " +
            "AND mc.marginType IN is.marginType " +
            "AND (e)-[:CLIENT_SIGNS]-(ag) " +
            "AND NOT (a)-[:EXCLUDED]->(mc) " +
            "WITH * " +
            "MATCH (e)-[:HAS]-(acc:TradingAccount) " +
            "WHERE (acc)-[:ACCESSES]->(ca) " +
            "RETURN a, nodes(assets), rels(assets)";

    @Override
    public Iterable<Asset> findEligibleAssetByClientId(ClientId clientId) {
        String query = ELIGIBLE_ASSET_WITH_ACCT_AND_TRANSFER_INFO;
        return session.query(getEntityType(), query, ImmutableMap.of("clientId",clientId.toString()));
    }

    @Override
    public Iterable<Asset> findReservedAssetByClientId(ClientId clientId) {
        String query = "MATCH (entity:LegalEntity)<-[:MANAGES]-(client:Client {id:{clientId}})-[:CLIENT_ASSET]->(:ClientAsset)-[possesses:POSSESSES]->(asset:Asset)-[:IS_AVAILABLE_FOR]->(agreement:Agreement) " +
                "WHERE (entity)-[:CLIENT_SIGNS]->(agreement) " +
                "AND possesses.reservedQuantities>0 " +
                "WITH asset " +
                "MATCH path=(asset)-[:IS_IN|HOLDS|POSSESSES]-() " +
                "RETURN asset, nodes(path), rels(path)";
        return session.query(getEntityType(), query, ImmutableMap.of("clientId",clientId.toString()));
    }

    @Override
    public Iterable<Asset> findAvailableAssetByClientIdAndCallIds(ClientId clientId, String... callIds) {
        String query = ELIGIBLE_ASSET_BY_CLIENT_AND_CALLIDS;
        return session.query(getEntityType(), query, ImmutableMap.of("clientId",clientId.toString(), "callIds", callIds));
    }

    @Override
    public Class<Asset> getEntityType() {
        return Asset.class;
    }
}
