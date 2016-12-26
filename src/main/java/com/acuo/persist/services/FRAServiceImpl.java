package com.acuo.persist.services;

import com.acuo.persist.entity.FRA;

import javax.inject.Singleton;

@Singleton
public class FRAServiceImpl extends GenericService<FRA> implements FRAService {
    @Override
    public Class<FRA> getEntityType() {
        return FRA.class;
    }
}
