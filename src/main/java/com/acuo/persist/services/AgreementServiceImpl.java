package com.acuo.persist.services;

import com.acuo.persist.entity.Agreement;
import com.acuo.persist.ids.PortfolioId;
import com.acuo.persist.ids.TradeId;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

public class AgreementServiceImpl extends GenericService<Agreement, String> implements AgreementService {

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
        return sessionProvider.get().queryForObject(getEntityType(), query, parameters);
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
        return sessionProvider.get().queryForObject(getEntityType(), query, parameters);
    }
}
