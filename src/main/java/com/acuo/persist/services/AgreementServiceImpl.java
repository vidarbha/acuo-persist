package com.acuo.persist.services;

import com.acuo.common.ids.ClientId;
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
    public Agreement find(String id) {
        String query =
                "MATCH p=(:Firm)-[:MANAGES]->(legal:LegalEntity)-[]->(agreement:Agreement {id:{id}})" +
                "RETURN p";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", id);
        return dao.getSession().queryForObject(getEntityType(), query, parameters);
    }

    @Override
    @Transactional
    public Agreement agreementFor(ClientId clientId, PortfolioId portfolioId) {
        String query =
            "MATCH (client:Client)-[:MANAGES]->(legal:LegalEntity)-[]->(agreement:Agreement)<-[:FOLLOWS]-(portfolio:Portfolio) " +
            "WHERE client.id = {clientId} AND portfolio.id = {portfolioId} " +
            "WITH portfolio " +
            "MATCH p=(:Firm)-[:MANAGES]->(:LegalEntity)-[]->(:Agreement)<-[:FOLLOWS]-(portfolio) " +
            "RETURN p";
        final ImmutableMap<String, String> parameters = ImmutableMap.of(
                "clientId", clientId.toString(),
                "portfolioId", portfolioId.toString());
        return dao.getSession().queryForObject(getEntityType(), query, parameters);
    }

    @Override
    @Transactional
    public Agreement agreementFor(ClientId clientId, TradeId tradeId) {
        String query =
           "MATCH (client:Client)-[:MANAGES]->(legal:LegalEntity)-[]->(agreement:Agreement)<-[:FOLLOWS]-(portfolio:Portfolio)" +
           "<-[BELONGS_TO]-(trade:Trade) " +
           "WHERE client.id = {clientId} AND trade.id = {tradeId} " +
           "WITH portfolio " +
           "MATCH p=(:Firm)-[:MANAGES]->(:LegalEntity)-[]->(:Agreement)<-[:FOLLOWS]-(portfolio) " +
           "RETURN p";
        final ImmutableMap<String, String> parameters = ImmutableMap.of(
                "clientId", clientId.toString(),
                "tradeId", tradeId.toString());
        return dao.getSession().queryForObject(getEntityType(), query, parameters);
    }
}
