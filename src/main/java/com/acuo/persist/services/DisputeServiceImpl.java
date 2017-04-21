package com.acuo.persist.services;

import com.acuo.persist.entity.Dispute;
import com.acuo.persist.ids.MarginStatementId;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

public class DisputeServiceImpl extends GenericService<Dispute, String> implements DisputeService {

    @Override
    public Class<Dispute> getEntityType() {
        return Dispute.class;
    }

    @Override
    @Transactional
    public Dispute relatedTo(MarginStatementId marginStatementId) {
        String query = "MATCH (dispute:Dispute)-[:ON]->(:MarginStatement {id:{id}) RETURN dispute";
        ImmutableMap<String, String> parameters = ImmutableMap.of("id", marginStatementId.toString());
        return sessionProvider.get().queryForObject(Dispute.class, query, parameters);
    }
}
