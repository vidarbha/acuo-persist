package com.acuo.persist.services;

import com.acuo.common.model.ids.MarginStatementId;
import com.acuo.persist.entity.Dispute;
import com.acuo.persist.entity.MarginStatement;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class DisputeServiceImpl extends AbstractService<Dispute, String> implements DisputeService {

    @Inject
    public DisputeServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<Dispute> getEntityType() {
        return Dispute.class;
    }

    @Override
    @Transactional
    public Dispute relatedTo(MarginStatementId marginStatementId) {
        String query = "MATCH (dispute:Dispute)-[:ON]->(:MarginStatement {id:{id}}) RETURN dispute";
        ImmutableMap<String, String> parameters = ImmutableMap.of("id", marginStatementId.toString());
        return dao.getSession().queryForObject(Dispute.class, query, parameters);
    }

    @Override
    @Transactional
    public Dispute add(MarginStatement statement, com.acuo.common.model.margin.Dispute dispute) {

        Dispute disputeInDB = new com.acuo.persist.entity.Dispute();
        disputeInDB.setMtm(dispute.getMtm());
        disputeInDB.setAgreedAmount(dispute.getAgreedAmount());
        disputeInDB.setBalance(dispute.getBalance());
        disputeInDB.setComments(dispute.getComments());
        disputeInDB.setDisputedAmount(dispute.getDisputedAmount());
        disputeInDB.setDisputeReasonCodes(dispute.getDisputeReasonCodes());
        disputeInDB.setIm(dispute.getIm());
        disputeInDB.setMarginStatement(statement);

        return createOrUpdate(disputeInDB);
    }
}
