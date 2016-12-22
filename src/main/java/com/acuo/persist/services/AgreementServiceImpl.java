package com.acuo.persist.services;

import com.acuo.persist.entity.Agreement;

public class AgreementServiceImpl extends GenericService<Agreement> implements AgreementService{

    @Override
    public Class<Agreement> getEntityType() {
        return Agreement.class;
    }
}
