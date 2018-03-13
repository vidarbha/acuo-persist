package com.acuo.persist.services;

import com.acuo.common.model.ids.AssetId;
import com.acuo.common.model.ids.ClientId;
import com.acuo.persist.entity.Asset;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;

public class AssetServiceImpl extends AbstractService<Asset, AssetId> implements AssetService {

    final static String AVAILABLE_ASSET =
            "MATCH (client:Client {id:{clientId}})-[:MANAGES]->(entity:LegalEntity)-[:CLIENT_SIGNS]->(agreement:Agreement)-[:IS_COMPOSED_OF]->(rule:Rule)-[:APPLIES_TO]->(asset:Asset) " +
            "WITH asset, client, rule " +
            "MATCH h=(:Custodian)-[:MANAGES]->(ca:CustodianAccount)-[holds:HOLDS]->(asset) " +
            "MATCH v=(asset)<-[:VALUATED]-(:AssetValuation)-[:VALUE]->(:AssetValue) " +
            "RETURN asset, " +
            "nodes(h), relationships(h), " +
            "nodes(v), relationships(v)";

    final static String ELIGIBLE_ASSET_BY_CLIENT_AND_CALLID =
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

    final static String TOTAL_HAIRCUT =
            "MATCH (si:StatementItem {id:{callId}})-[:PART_OF]->(:MarginStatement)-[:STEMS_FROM]->(agr:Agreement) " +
            "MATCH (a:Asset {id:{assetId}})-[:IS_IN]->(:AssetCategory)-[eu:IS_ELIGIBLE_UNDER]->(agr) " +
            "WITH eu.haircut + eu.FXHaircut as totalHaircut " +
            "RETURN totalHaircut";

    @Inject
    public AssetServiceImpl(Provider<Session> session) {
        super(session);
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
    public Class<Asset> getEntityType() {
        return Asset.class;
    }
}
