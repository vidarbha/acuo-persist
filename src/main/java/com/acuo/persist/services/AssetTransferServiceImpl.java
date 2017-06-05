package com.acuo.persist.services;

import com.acuo.persist.entity.AssetTransfer;
import org.neo4j.ogm.model.Result;

import java.util.Collections;

public class AssetTransferServiceImpl extends GenericService<AssetTransfer, String> implements AssetTransferService {

    @Override
    public Class<AssetTransfer> getEntityType() {
        return AssetTransfer.class;
    }

    public Result getPledgedAssets() {
        String query = "MATCH (assets:Asset)<-[:OF]-(at:AssetTransfer)-[:GENERATED_BY]->(:MarginCall)-[:PART_OF]->(:MarginStatement)-[:STEMS_FROM]->(a:Agreement) " +
                "WHERE at.status = 'Departed' " +
                "MATCH (l1:LegalEntity)-[:CLIENT_SIGNS]->(a)<-[:COUNTERPARTY_SIGNS]-(l2:LegalEntity) " +
                "MATCH (c1:Custodian)-[:MANAGES]->(ca1:CustodianAccount)<-[:FROM]-(at)-[:TO]->(ca2:CustodianAccount)<-[:MANAGES]-(c2:Custodian) " +
                "RETURN at.id, at.pledgeTime, a.name, l1.name, l2.name, a.currency, at.subStatus, c1.name, c2.name, ca1.name, ca2.name, at.quantities, assets.currency,assets.name, assets.id";

        return sessionProvider.get().query(query, Collections.emptyMap());

    }
}