package com.acuo.persist.services;

import com.acuo.persist.entity.Portfolio;
import com.acuo.persist.ids.TradeId;
import com.google.common.collect.ImmutableMap;
import com.google.inject.persist.Transactional;

public class PortfolioServiceImpl extends GenericService<Portfolio> implements PortfolioSerqvice {

    @Override
    public Class<Portfolio> getEntityType() {
        return Portfolio.class;
    }

    @Override
    @Transactional
    public Portfolio findBy(TradeId tradeId) {
        String query = "MATCH p=(p:Portfolio)<-[:BELONGS_TO]-(:Trade {id:{id}}) RETURN p, nodes(p), rels(p)";
        final ImmutableMap<String, String> parameters = ImmutableMap.of("id", tradeId.toString());
        return sessionProvider.get().queryForObject(Portfolio.class, query, parameters);
    }
}
