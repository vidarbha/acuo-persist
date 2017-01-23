package com.acuo.persist.services;

import com.acuo.persist.entity.Asset;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

@Transactional
public class AssetServiceImpl extends GenericService<Asset> implements AssetService{

    @Override
    public Iterable<Asset> findEligibleAssetByClientId(String clientId) {
        String query = "MATCH (entity:LegalEntity)<-[:MANAGES]-(client:Client {id:{clientId}})-[:CLIENT_ASSET]->(:ClientAsset)-[possesses:POSSESSES]->(asset:Asset)-[:IS_AVAILABLE_FOR]->(agreement:Agreement) " +
                        "WHERE (entity)-[:CLIENT_SIGNS]->(agreement) " +
                        "AND possesses.quantities>possesses.reservedQuantities " +
                        "WITH asset " +
                        "MATCH path=(asset)-[:IS_IN|HOLDS|POSSESSES]-() " +
                        "RETURN asset, nodes(path), rels(path)";
        return session.query(getEntityType(), query, ImmutableMap.of("clientId",clientId));
    }

    @Override
    public Iterable<Asset> findReservedAssetByClientId(String clientId) {
        String query = "MATCH (entity:LegalEntity)<-[:MANAGES]-(client:Client {id:{clientId}})-[:CLIENT_ASSET]->(:ClientAsset)-[possesses:POSSESSES]->(asset:Asset)-[:IS_AVAILABLE_FOR]->(agreement:Agreement) " +
                "WHERE (entity)-[:CLIENT_SIGNS]->(agreement) " +
                "AND possesses.reservedQuantities>0 " +
                "WITH asset " +
                "MATCH path=(asset)-[:IS_IN|HOLDS|POSSESSES]-() " +
                "RETURN asset, nodes(path), rels(path)";
        return session.query(getEntityType(), query, ImmutableMap.of("clientId",clientId));
    }

    @Override
    public Class<Asset> getEntityType() {
        return Asset.class;
    }
}
