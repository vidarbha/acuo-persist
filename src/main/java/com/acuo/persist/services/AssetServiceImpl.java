package com.acuo.persist.services;

import com.acuo.common.ids.AssetId;
import com.acuo.common.ids.ClientId;
import com.acuo.persist.entity.Asset;
import com.acuo.persist.entity.Holds;
import com.acuo.persist.entity.SettlementDate;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class AssetServiceImpl extends AbstractService<Asset, AssetId> implements AssetService {

    private final HoldsService holdsService;

    @Inject
    public AssetServiceImpl(Provider<Session> session, HoldsService holdsService) {
        super(session);
        this.holdsService = holdsService;
    }

    private final static String AVAILABLE_ASSET =
            "MATCH h=(client:Client)-[:HAS]->(ca)-[holds:HOLDS]->(asset:Asset) " +
            "WHERE client.id = {clientId} " +
            "MATCH s=(asset)-[:SETTLEMENT*0..1]->(settlement)-[:LATEST*0..1]->(settlementDate) " +
            "RETURN h, s";

    private final static String AVAILABLE_ASSET_OLD =
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
            "WITH agr " +
            "MATCH (asset:Asset)-[:IS_IN]->()-[eu:IS_ELIGIBLE_UNDER]->(agr) " +
            "WHERE asset.id = {assetId} " +
            "WITH eu.haircut + eu.FXHaircut as totalHaircut " +
            "RETURN totalHaircut";

    private final static String ASSET_INVENTORY =
            "MATCH (client:Client)-[:HAS]->(ca)-[:HOLDS]->(asset) " +
            "WHERE client.id = {clientId} " +
                    "WITH DISTINCT asset " +
                    "MATCH p=()-[:MANAGES]->()-[:HOLDS]->(asset)-[*0..1]-()" +
                    "RETURN p";

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
        Iterator<Map<String, Object>> iterator = result.iterator();
        if (iterator.hasNext()) {
            Map<String, Object> next = iterator.next();
            return (Double) next.get("totalHaircut");
        } else {
            return 0.0d;
        }
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
    public void addQuantity(AssetId assetId, Double quantity) {
        quantity(assetId, quantity, (a, b) -> a + b);
    }

    @Override
    public void removeQuantity(AssetId assetId, Double quantity) {
        quantity(assetId, quantity, (a, b) -> a - b);
    }

    private void quantity(AssetId assetId, Double quantity, BinaryOperator<Double> op) {
        Asset asset = find(assetId, 1);
        Holds holds = asset.getHolds();
        if (holds != null) {
            final Double result = op.apply(holds.getAvailableQuantity(), quantity);
            holds.setAvailableQuantity(result);
            holdsService.save(holds, 1);
        }
    }

    @Override
    public Class<Asset> getEntityType() {
        return Asset.class;
    }
}
