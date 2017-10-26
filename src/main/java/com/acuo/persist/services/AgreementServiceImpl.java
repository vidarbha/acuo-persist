package com.acuo.persist.services;

import com.acuo.common.ids.PortfolioId;
import com.acuo.common.ids.TradeId;
import com.acuo.persist.entity.Agreement;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;
import org.neo4j.ogm.session.Session;

import javax.inject.Inject;
import javax.inject.Provider;

public class AgreementServiceImpl extends AbstractService<Agreement, String> implements AgreementService {

    @Inject
    public AgreementServiceImpl(Provider<Session> session) {
        super(session);
    }

    @Override
    public Class<Agreement> getEntityType() {
        return Agreement.class;
    }

    @Override
    @Transactional
    public Agreement agreementFor(PortfolioId portfolioId) {
        String query =
                "MATCH p=(legal:LegalEntity)-[]->(agreement:Agreement)<-[:FOLLOWS]-(portfolio:Portfolio {id:{id}}) " +
                "RETURN p, nodes(p), relationships(p)";
        final String pId = portfolioId.toString();
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", pId);
        return dao.getSession().queryForObject(getEntityType(), query, parameters);
    }

    @Override
    @Transactional
    public Agreement agreementFor(TradeId tradeId) {
        String query =
                "MATCH p=(legal:LegalEntity)-[]->(agreement:Agreement)<-[:FOLLOWS]-(portfolio:Portfolio)" +
                "<-[BELONGS_TO]-(trade:Trade {id:{id}}) " +
                "RETURN p, nodes(p), relationships(p)";
        final String id = tradeId.toString();
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", id);
        return dao.getSession().queryForObject(getEntityType(), query, parameters);
    }
}
