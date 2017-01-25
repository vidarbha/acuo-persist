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
    public Class<Asset> getEntityType() {
        return Asset.class;
    }
}
