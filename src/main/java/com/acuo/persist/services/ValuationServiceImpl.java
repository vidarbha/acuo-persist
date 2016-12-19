package com.acuo.persist.services;

import com.acuo.persist.entity.Valuation;

public class ValuationServiceImpl extends GenericService<Valuation> implements ValuationService{

    @Override
    public Class<Valuation> getEntityType() {
        return Valuation.class;
    }
}
