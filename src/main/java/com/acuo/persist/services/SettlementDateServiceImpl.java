package com.acuo.persist.services;

import com.acuo.persist.entity.SettlementDate;

public class SettlementDateServiceImpl extends GenericService<SettlementDate, String> implements SettlementDateService {

    @Override
    public Class<SettlementDate> getEntityType() {
        return SettlementDate.class;
    }
}