package com.acuo.persist.services;

import com.acuo.persist.entity.Asset;
import com.google.common.collect.ImmutableMap;

public class AssetServiceImpl extends GenericService<Asset> implements AssetService{

    @Override
    public Iterable<Asset> findEligibleAssetByClientId(String clientId) {
        String query = "MATCH (entity:LegalEntity)<-[:MANAGES]-(client:Client {id:{clientId}})-[possesses:POSSESSES]->(asset:Asset)-[:IS_AVAILABLE_FOR]->(agreement:Agreement) " +
                        "WHERE (entity)-[:CLIENT_SIGNS]->(agreement) " +
                        "MATCH path=(asset)-[:IS_IN|HOLDS|POSSESSES]-() " +
                        "RETURN asset, nodes(path), rels(path)";
        return session.query(getEntityType(), query, ImmutableMap.of("clientId",clientId));
    }

    @Override
    public Class<Asset> getEntityType() {
        return Asset.class;
    }
}
