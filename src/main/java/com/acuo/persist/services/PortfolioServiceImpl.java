package com.acuo.persist.services;

import com.acuo.persist.entity.Portfolio;

public class PortfolioServiceImpl extends GenericService<Portfolio> implements PortfolioService {

    @Override
    public Class<Portfolio> getEntityType() {
        return Portfolio.class;
    }
}
